package com.example.test


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.network.AudioFile
import com.example.test.network.Product
import com.example.test.network.room.dao.ProductDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class TestViewModel(
    private val repo: Repo,
) : ViewModel() {

    val users = MutableStateFlow<List<User>>(emptyList())
    val data = MutableStateFlow("")

    val uiState = MutableStateFlow<UIState>(UIState.Default)

    val saveData = MutableStateFlow("")

    val products = MutableStateFlow<List<Product>>(emptyList())

    val favorites= MutableStateFlow<List<Product>>(emptyList())

    val audioFiles=MutableStateFlow<List<AudioFile>>(emptyList())

    fun getAudioFiles(){
        viewModelScope.launch {
            audioFiles.value = repo.getAudioFiles()
        }

    }



    fun getProducts() {
        viewModelScope.launch {
            val result = repo.getProducts()
            if (result.isSuccess) {
                products.value = result.getOrDefault(emptyList())
                uiState.value = UIState.Success(products.value)

            } else {
                uiState.value = UIState.Error(result.exceptionOrNull()?.message.toString())
            }
        }
    }

    fun addProductToFavorite(product: Product) {
        viewModelScope.launch {
            repo.insertFavoriteProduct(product)
            uiState.value = UIState.Success(true)
        }
    }

    fun deleteFavoriteProduct(product: Product) {
        viewModelScope.launch {
            repo.deleteFavoriteProduct(product)
            uiState.value = UIState.Success(true)
        }
    }

    fun showFavorites() {
        viewModelScope.launch {
             repo.getFavoriteProducts().collectLatest {
                     favorites.value=it

                 uiState.value = UIState.Success("LoadedData")

             }

        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            repo.signUp(email, password)
            uiState.value = UIState.Success(true)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiState.value = UIState.Loading
            delay(2000)
            val result = repo.login(email, password)
            if (result) {
                uiState.value = UIState.Success("Login Successful")
            } else {
                uiState.value = UIState.Error("Invalid Credentials")
            }
        }
    }

    fun save(data: String) {
        viewModelScope.launch {
            saveData.value = data
            repo.saveData(saveData.value)
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            uiState.value = UIState.Loading
            delay(5000)
            users.value = listOf(User(1, "Zain"), User(2, "Ali"))
            uiState.value = UIState.Success(users.value)

        }

    }


    fun resetState() {
        uiState.value = UIState.Default
    }


    fun load() {
        viewModelScope.launch {
            data.value = repo.getData()
        }
    }


}

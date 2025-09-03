package com.example.test

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.net.http.UrlRequest
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.edit
import com.example.test.network.AudioFile
import com.example.test.network.Product
import com.example.test.network.RetrofitInstance
import com.example.test.network.room.dao.ProductDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

class Repo(
    private val context: Context,
    private val dao: ProductDao
) {

    val pref = context.getSharedPreferences("test", Context.MODE_PRIVATE)

    fun saveData(data: String) {
        pref.edit {
            putString("data", data).apply()
        }
    }

    fun getData(): String {
        return pref.getString("data", "") ?: ""
    }

    fun signUp(email:String, password:String){
        pref.edit(){
            putString("email",email).commit()
            putString("password",password).commit()

        }
    }

    fun login(email: String,password: String):Boolean{
        val savedEmail = pref.getString("email","")
        val savedPassword = pref.getString("password","")


        return savedEmail == email && savedPassword == password
    }
    suspend fun getProducts(): Result<List<Product>> {
        val response = RetrofitInstance.api.getProducts()

        return  if (response.code()==200){
             Result.success(response.body() ?: emptyList())
        }else{
            Result.failure(Exception( mapCodeToError(response.code())))
        }

    }

     fun getFavoriteProducts(): Flow<List<Product>> {
         return dao.getFavoriteProducts()
     }

    suspend fun insertFavoriteProduct(product: Product){
        dao.addProductToFavorite(product)

    }
    suspend fun deleteFavoriteProduct(product: Product) {
        dao.deleteFavoriteProduct(product)
    }

    fun getAudioFiles():List<AudioFile> {
        try {
            val projection=arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
            )
            val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
            val sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC"
            val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder

            )
            val audioFiles = mutableListOf<AudioFile>()
            cursor.use {
                val idColumn = it?.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleColumn = it?.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val durationColumn = it?.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val dataColumn = it?.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)


                while (it?.moveToNext() ?: false) {
                    val id = it.getLong(idColumn ?: 0)
                    val title = it.getString(titleColumn ?: 0)
                    val duration = it.getLong(durationColumn ?: 0)
                    val path = it.getString(dataColumn ?: 0)
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                    audioFiles.add(AudioFile(id, title ?: "", duration, path ?: "", uri.toString()))


                }
            }
            return audioFiles
        } catch (e: Exception) {
            throw Exception(e.message)
        } catch (e: SecurityException) {
            throw Exception(e.message)
        }


    }


}

fun mapCodeToError(errorCode: Int): String{
    return when(errorCode){
        404-> "Connection Forbidden"
        else -> "Something went wrong"
    }
}
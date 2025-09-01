package com.example.test.network.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.test.network.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Dao
interface ProductDao {
    @Insert()
    suspend fun addProductToFavorite(product: Product)

    @Delete
    suspend fun deleteFavoriteProduct(product: Product)

    @Query("SELECT * FROM Product")
    fun getFavoriteProducts(): Flow<List<Product>>

}
package com.example.test.network

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("products")
    suspend fun getProducts(): Response<List<Product>>
}
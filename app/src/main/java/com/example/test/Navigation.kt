package com.example.test

import android.icu.text.CaseMap
import com.example.test.network.Product
import kotlinx.serialization.Serializable


sealed interface Routes{
    @Serializable
    object ScreenA

    @Serializable
    data class ScreenB(val v:List<Int>)

    @Serializable
    object LoginScreen

    @Serializable
    object SignUpScreen

    @Serializable
    object EcommerceDashboardScreen

    @Serializable
    object FavoriteScreen

    @Serializable
    data class ProductDetailScreen(
        val id:Int?,
        val title: String,
        val image:String,
        val price:Double,
        val description:String
    )

    @Serializable
    object LocalStorageTestScreen

    @Serializable
    data class AudioPlayerScreen(
        val audioUri:String
    )
}



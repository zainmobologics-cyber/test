package com.example.test.network

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity()
@Serializable
data class Product(
    val category: String,
    val description: String,
    val image: String,
    val price: Double,
    val rating: Rating,
    val title: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
)
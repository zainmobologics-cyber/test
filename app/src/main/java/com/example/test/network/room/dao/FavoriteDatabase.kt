package com.example.test.network.room.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.test.network.Product

@Database(entities = [Product::class], version = 1)
@TypeConverters(Convertors::class)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao

}


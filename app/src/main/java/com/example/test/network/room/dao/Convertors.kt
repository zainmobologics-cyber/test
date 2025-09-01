package com.example.test.network.room.dao

import androidx.room.TypeConverter
import com.example.test.network.Rating

class Convertors {

    @TypeConverter
    fun fromRating(rating: Rating): String {
        return "${rating.count},${rating.rate}"
    }

    @TypeConverter
    fun toRating(ratingString: String): Rating {
        val parts = ratingString.split(",")
        return Rating(parts[0].toInt(), parts[1].toDouble())

    }


}
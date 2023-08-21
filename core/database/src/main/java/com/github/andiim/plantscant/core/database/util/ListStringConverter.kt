package com.github.andiim.plantscant.core.database.util

import androidx.room.TypeConverter

object ListStringConverter {
    @TypeConverter
    fun fromListString(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toListString(data: String): List<String> {
        return data.split(",")
    }
}
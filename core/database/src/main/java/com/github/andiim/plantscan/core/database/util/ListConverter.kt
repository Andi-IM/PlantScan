package com.github.andiim.plantscan.core.database.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ListConverter {
    @TypeConverter
    fun stringToList(value: String?): List<String>? =
        value.let {
            val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
            return Gson().fromJson(it, listType)
        }

    @TypeConverter
    fun listToString(value: List<String>?): String? =
        value?.let(Gson()::toJson)
}

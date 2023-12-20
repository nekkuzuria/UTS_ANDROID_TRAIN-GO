package com.example.uts_traingo.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BooleanArrayConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromBooleanList(value: MutableList<Boolean>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toBooleanList(value: String?): MutableList<Boolean>? {
        val listType = object : TypeToken<MutableList<Boolean>>() {}.type
        return gson.fromJson(value, listType)
    }
}

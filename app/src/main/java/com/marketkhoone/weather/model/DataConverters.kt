package com.marketkhoone.weather.model

import androidx.room.TypeConverter
import com.marketkhoone.weather.model.entity.Daily
import com.marketkhoone.weather.model.entity.Hourly
import com.marketkhoone.weather.model.entity.Weather
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object DataConverter {

    @TypeConverter
    @JvmStatic
    fun weatherStringToList(data: String?): List<Weather>? {
        if (data == null) {
            return emptyList()
        }

        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Weather::class.java)
        val adapter = moshi.adapter<List<Weather>>(type)
        return adapter.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun weatherListToString(objects: List<Weather>): String {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Weather::class.java)
        val adapter = moshi.adapter<List<Weather>>(type)
        return adapter.toJson(objects)
    }

    @TypeConverter
    @JvmStatic
    fun dailyStringToList(data: String?): List<Daily>? {
        if (data == null) {
            return emptyList()
        }

        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Daily::class.java)
        val adapter = moshi.adapter<List<Daily>>(type)
        return adapter.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun dailyListToString(objects: List<Daily>): String {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Daily::class.java)
        val adapter = moshi.adapter<List<Daily>>(type)
        return adapter.toJson(objects)
    }

    @TypeConverter
    @JvmStatic
    fun hourlyStringToList(data: String?): List<Hourly>? {
        if (data == null) {
            return emptyList()
        }

        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Hourly::class.java)
        val adapter = moshi.adapter<List<Hourly>>(type)
        return adapter.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun hourlyListToString(objects: List<Hourly>): String {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, Hourly::class.java)
        val adapter = moshi.adapter<List<Hourly>>(type)
        return adapter.toJson(objects)
    }
}

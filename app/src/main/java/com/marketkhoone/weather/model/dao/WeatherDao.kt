package com.marketkhoone.weather.model.dao

import androidx.room.*
import com.marketkhoone.weather.model.entity.ForecastWeather


@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertForecastWeather(forecastWeather: ForecastWeather)

    @Insert
    suspend fun insertAllForecastWeathers(vararg forecastWeathers: ForecastWeather): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateForecastWeather(forecastWeather: ForecastWeather?)

    @Query("SELECT * FROM forecastWeather")
    suspend fun getAllForecastWeathers():List<ForecastWeather>

    @Query("SELECT * FROM forecastWeather WHERE uuid= :forecastWeatherId")
    suspend fun getForecastWeather(forecastWeatherId: Int): ForecastWeather

    @Query("SELECT * FROM forecastWeather WHERE active = 1")
    suspend fun getActiveForecastWeather(): ForecastWeather?

    @Query("UPDATE forecastWeather SET active = 1 WHERE uuid = (SELECT MAX(uuid) FROM forecastWeather)")
    suspend fun setActiveLastForecastWeather()

    @Query("UPDATE forecastWeather SET active = 1 WHERE uuid = :forecastWeatherId")
    suspend fun setActiveForecastWeatherById(forecastWeatherId: Int)

    @Query("UPDATE forecastWeather SET active = 0 WHERE active = 1")
    fun updateForecastWeatherActive()

    @Query("DELETE FROM forecastWeather")
    suspend fun deleteAllForecastWeathers()

    @Query("DELETE FROM forecastWeather WHERE uuid= :forecastWeatherId")
    suspend fun deleteForecastWeatherById(forecastWeatherId: Int)
}
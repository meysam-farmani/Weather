package com.marketkhoone.weather.model

import com.marketkhoone.weather.model.entity.ForecastWeather
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeathersApiService {
    private val BASE_URL = "https://api.openweathermap.org/"

    private val EXCLUDE = "minutely"
    private val UNITS = "metric"
    private val APPID = "12519a3ef515fc973b794a4a6bbe5a69"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    fun getForecastWeather(lat: String?,lon: String?): Single<ForecastWeather> {
        return api.getForecastWeather(lat,lon,EXCLUDE,UNITS,APPID)
//        return api.getWeather()
    }
}
package com.marketkhoone.weather.model

import com.marketkhoone.weather.model.entity.ForecastWeather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

//    @GET("forecastrss?location=tehran&format=json&oauth_consumer_key=dj0yJmk9UUNSTE5xNmdYT1drJmQ9WVdrOU1rbE9WRVZIYTA4bWNHbzlNQT09JnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PTg3&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1600172570&oauth_nonce=ZTFxh03ENAD&oauth_version=1.0&oauth_signature=Hptmk2H9sTDpjCNcpB4yhIm8eZk=")
//    fun getWeather(): Single<Weather>

    @GET("data/2.5/onecall")
    fun getForecastWeather(@Query("lat") lat: String?,
                           @Query("lon") lon: String?,
                           @Query("exclude") exclude: String?,
                           @Query("units") units: String?,
                           @Query("appid") appid: String?, ): Single<ForecastWeather>
}
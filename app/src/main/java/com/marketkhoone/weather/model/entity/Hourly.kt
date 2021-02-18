package com.marketkhoone.weather.model.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Hourly (
	@Json(name = "dt") 
	var dt : Int?,
	@Json(name = "temp") 
	var temp : String?,
	@Json(name = "feels_like") 
	var feels_like : String?,
	@Json(name = "pressure")
	var pressure : Int?,
	@Json(name = "humidity")
	var humidity : Int?,
	@Json(name = "dew_point")
	var dew_point : String?,
	@Json(name = "clouds")
	var clouds : Int?,
	@Json(name = "visibility")
	var visibility : Int?,
	@Json(name = "wind_speed")
	var wind_speed : String?,
	@Json(name = "wind_deg") 
	var wind_deg : Int?,
	@Json(name = "weather")
	var weather : List<Weather?>? = null,
//	@Json(name = "pop")
//	var pop : Int?
) : Parcelable {

}
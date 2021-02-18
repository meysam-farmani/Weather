package com.marketkhoone.weather.model.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Daily(
	@Json(name = "dt")
	var dt: Int?,
	@Json(name = "sunrise")
	var sunrise: Int?,
	@Json(name = "sunset")
	var sunset: Int?,
	@Json(name = "temp")
	var temp: Temp?,
	@Json(name = "feels_like")
	var feels_like: Feels_like?,
	@Json(name = "pressure")
	var pressure: Int?,
	@Json(name = "humidity")
	var humidity: Int?,
	@Json(name = "dew_point")
	var dew_point: String?,
	@Json(name = "wind_speed")
	var wind_speed: String?,
	@Json(name = "wind_deg")
	var wind_deg: Int?,
	@Json(name = "weather")
	var weather: List<Weather?>? = null,
	@Json(name = "clouds")
	var clouds: Int?,
	@Json(name = "pop")
	var pop: String?,
	@Json(name = "uvi")
	var uvi: String?,
	var expand: Boolean = false

) : Parcelable {
	fun setExpanded(expanded: Boolean) {
		this.expand = expanded
	}

	fun isExpanded(): Boolean {
		return expand
	}
}
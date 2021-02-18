package com.marketkhoone.weather.model.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Weather(

	@Json(name ="id")
	var id: Int?,
	@Json(name ="main")
	var main: String?,
	@Json(name ="description")
	var description: String?,
	@Json(name ="icon")
	var icon: String?
) : Parcelable {

}
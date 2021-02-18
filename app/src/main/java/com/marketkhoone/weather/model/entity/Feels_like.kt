package com.marketkhoone.weather.model.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Feels_like (

	@Json(name ="day")
	var day : String?,
	@Json(name ="night")
	var night : String?,
	@Json(name ="eve")
	var eve : String?,
	@Json(name ="morn")
	var morn : String?
) : Parcelable {

}
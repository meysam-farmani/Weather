package com.marketkhoone.weather.model.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Temp (

	@Json(name ="day")
	var day : String?,
	@Json(name ="min")
	var min : String?,
	@Json(name ="max")
	var max : String?,
	@Json(name ="night")
	var night : String?,
	@Json(name ="eve")
	var eve : String?,
	@Json(name ="morn")
	var morn : String?
) : Parcelable{

}
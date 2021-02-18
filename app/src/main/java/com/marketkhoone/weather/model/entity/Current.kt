package com.marketkhoone.weather.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "current")
data class Current (
	@ColumnInfo(name ="dt")
	var dt : Int?,
	@ColumnInfo(name ="sunrise")
	var sunrise : Int?,
	@ColumnInfo(name ="sunset")
	var sunset : Int?,
	@ColumnInfo(name ="temp")
	var temp : String?,
	@ColumnInfo(name ="feels_like")
	var feels_like : String?,
	@ColumnInfo(name ="pressure")
	var pressure : Int?,
	@ColumnInfo(name ="humidity")
	var humidity : Int?,
	@ColumnInfo(name ="dew_point")
	var dew_point : String?,
	@ColumnInfo(name ="uvi")
	var uvi : String?,
	@ColumnInfo(name ="clouds")
	var clouds : Int?,
	@ColumnInfo(name ="visibility")
	var visibility : Int?,
	@ColumnInfo(name ="wind_speed")
	var wind_speed : String?,
	@ColumnInfo(name ="wind_deg")
	var wind_deg : Int?,
	@ColumnInfo(name ="weather")
	var weather : List<Weather?>? = null
) : Parcelable {
	@Ignore
	constructor(current: Current?) : this(
		dt = current?.dt,
		sunrise = current?.sunrise,
		sunset = current?.sunset,
		temp = current?.temp,
		feels_like = current?.feels_like,
		pressure = current?.pressure,
		humidity = current?.humidity,
		dew_point = current?.dew_point,
		uvi = current?.uvi,
		clouds = current?.clouds,
		visibility = current?.visibility,
		wind_speed = current?.wind_speed,
		wind_deg = current?.wind_deg,
		weather = current?.weather,
	)
}
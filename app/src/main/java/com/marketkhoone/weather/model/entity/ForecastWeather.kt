package com.marketkhoone.weather.model.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "forecastWeather")
data class ForecastWeather (
    @PrimaryKey(autoGenerate = true)
    var uuid : Int = 0,
    @ColumnInfo(name ="lat")
    var lat : String?,
    @ColumnInfo(name ="lon")
    var lon : String?,
    @ColumnInfo(name ="city")
    var city : String?,
    @ColumnInfo(name ="country")
    var country : String?,
    @ColumnInfo(name ="active")
    var active : Boolean?,
    @ColumnInfo(name ="timezone")
    var timezone : String?,
    @ColumnInfo(name ="timezone_offset")
    var timezone_offset : Int?,
    @Embedded
    var current : Current?,
    @ColumnInfo(name ="hourly")
    var hourly : List<Hourly?>? = null,
    @ColumnInfo(name ="daily")
    var daily : List<Daily?>? = null
) : Parcelable {
    @Ignore
    constructor(forecastWeather: ForecastWeather?) : this(
        lat = forecastWeather?.lat,
        lon = forecastWeather?.lon,
        city = forecastWeather?.city,
        country = forecastWeather?.country,
        active = forecastWeather?.active,
        timezone = forecastWeather?.timezone,
        timezone_offset = forecastWeather?.timezone_offset,
        current = Current(forecastWeather?.current),
        hourly = forecastWeather?.hourly,
        daily = forecastWeather?.daily,
    )
}
package com.marketkhoone.weather.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marketkhoone.weather.model.dao.WeatherDao
import com.marketkhoone.weather.model.entity.*


@Database(
    entities = [ForecastWeather::class],
//    entities = [Weather::class, Astronomy::class, Atmosphere::class, Condition::class, Current_observation::class, Forecast::class, Location::class, Wind::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "weatherdatabase"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

}
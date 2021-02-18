package com.marketkhoone.weather.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.marketkhoone.weather.model.WeathersApiService
import com.marketkhoone.weather.model.WeatherDatabase
import com.marketkhoone.weather.model.entity.ForecastWeather
import com.marketkhoone.weather.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException


class ExploreViewModel(application: Application) : BaseViewModel(application) {
    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 20 * 60 * 1000 * 1000 * 1000L
    private val weathersApiService = WeathersApiService()
    private val disposable = CompositeDisposable()

    val forecastWeatherLiveData = MutableLiveData<ForecastWeather>()
    private var forecastWeathersList = ArrayList<ForecastWeather>()
    val loading = MutableLiveData<Boolean>()

    fun fetch() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            fetchFromDatabase()
        }else{
            fetchAllList()
        }
    }

    private fun fetchFromDatabase(){
        loading.value = true
        launch {
            val dao = WeatherDatabase(getApplication()).weatherDao()
            var forecastWeather = dao.getActiveForecastWeather()
            if(forecastWeather == null){
                dao.setActiveLastForecastWeather()
                forecastWeather = dao.getActiveForecastWeather()
            }
            forecastWeatherLiveData.value = forecastWeather
        }
    }

    private fun checkCacheDuration(){
        val cachePreference = prefHelper.getCacheDuration()

        try{
            val cachePreferenceInt = cachePreference?.toInt() ?: 10 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        }catch (e: NumberFormatException){
            e.printStackTrace()
        }
    }

    private fun fetchAllList(){
        loading.value = true
        launch {
            val dao = WeatherDatabase(getApplication()).weatherDao()
            val allForecastWeathers = dao.getAllForecastWeathers()
            forecastWeathersList.clear()

            if(allForecastWeathers.size > 0){
                for (forecastWeather in allForecastWeathers){
                    fetchListFromRemote(forecastWeather,allForecastWeathers.size)
                }
            }else{
                forecastWeatherLiveData.value = null
                loading.value = false
            }
        }
    }

    private fun fetchListFromRemote(item: ForecastWeather, size : Int) {

        disposable.add(
            weathersApiService.getForecastWeather(item.lat, item.lon)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ForecastWeather>() {
                    override fun onSuccess(forecastWeather : ForecastWeather) {
                        checkForecastWeathersList(forecastWeather, item, size)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun checkForecastWeathersList(forecastWeather: ForecastWeather, item: ForecastWeather, size: Int){
        forecastWeather.city = item.city
        forecastWeather.country = item.country
        forecastWeather.uuid = item.uuid
        forecastWeathersList.add(forecastWeather)
        if(forecastWeathersList.size == size){
            storeForecastWeathersListLocally(forecastWeathersList)
        }
    }

    private fun storeForecastWeathersListLocally(forecastWeathersList: List<ForecastWeather>){
        launch {
            val forecastWeathers = forecastWeathersList.sortedBy {
                it.uuid
            }
            val dao = WeatherDatabase(getApplication()).weatherDao()
            dao.deleteAllForecastWeathers()
            val result = dao.insertAllForecastWeathers(*forecastWeathers.toTypedArray())
            var i=0
            while (i < forecastWeathers.size){
                forecastWeathers[i].uuid= result[i].toInt()
                ++i
            }
            var forecastWeather = dao.getActiveForecastWeather()
            if(forecastWeather == null){
                dao.setActiveLastForecastWeather()
                forecastWeather = dao.getActiveForecastWeather()
            }
            forecastWeathersRetrieved(forecastWeather)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    private fun forecastWeathersRetrieved(forecastWeather:ForecastWeather?){
        forecastWeatherLiveData.value = forecastWeather
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}
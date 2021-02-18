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
import java.util.*
import kotlin.collections.ArrayList


class HomeViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 20 * 60 * 1000 * 1000 * 1000L
    private val weathersApiService = WeathersApiService()
    private val disposable = CompositeDisposable()

    private var forecastWeathersList = ArrayList<ForecastWeather>()
    val forecastWeathers = MutableLiveData<List<ForecastWeather>>()
    val forecastWeathersLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun fetchList() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            fetchFromDatabase()
        }else{
            fetchAllList()
        }
    }

    fun refreshBypassCache(){
        fetchAllList()
    }

    private fun fetchAllList(){
        launch {
            val dao = WeatherDatabase(getApplication()).weatherDao()
            val allForecastWeathers = dao.getAllForecastWeathers()
            forecastWeathersList.clear()

            if(allForecastWeathers.size > 0){
                for (forecastWeather in allForecastWeathers){
                    fetchListFromRemote(forecastWeather,allForecastWeathers.size)
                }
            }else{
                forecastWeathers.value = ArrayList<ForecastWeather>()
                forecastWeathersLoadError.value = false
                loading.value = false
            }
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

    private fun fetchFromDatabase(){
        loading.value = true
        launch {
            val dao = WeatherDatabase(getApplication()).weatherDao()
            val forecastWeathers = dao.getAllForecastWeathers()
            forecastWeathersRetrieved(forecastWeathers)
        }
    }

    private fun fetchListFromRemote(item: ForecastWeather, size : Int) {
        loading.value = true

        disposable.add(
            weathersApiService.getForecastWeather(item.lat, item.lon)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ForecastWeather>() {
                    override fun onSuccess(forecastWeather : ForecastWeather) {
                        checkForecastWeathersList(forecastWeather, item, size)
                    }

                    override fun onError(e: Throwable) {
                        forecastWeathersLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    fun fetchFromRemote(lat : String?, lon : String?, city : String?, country : String?) {
        loading.value = true

        disposable.add(
            weathersApiService.getForecastWeather(lat, lon)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ForecastWeather>() {
                    override fun onSuccess(forecastWeather: ForecastWeather) {
                        storeForecastWeathersLocally(forecastWeather, city, country)
                    }

                    override fun onError(e: Throwable) {
                        forecastWeathersLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun storeForecastWeathersLocally(forecastWeather: ForecastWeather, city: String?, country: String?){
        launch {
            forecastWeather.city = city
            forecastWeather.country = country
            forecastWeather.active = true
            val dao = WeatherDatabase(getApplication()).weatherDao()
            dao.updateForecastWeatherActive()
            dao.insertForecastWeather(forecastWeather)
            val forecastWeathers = dao.getAllForecastWeathers()
            forecastWeathersRetrieved(forecastWeathers)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
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
            forecastWeathersRetrieved(forecastWeathers)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    private fun forecastWeathersRetrieved(forecastWeathers:List<ForecastWeather>){
        this.forecastWeathers.value = forecastWeathers
        forecastWeathersLoadError.value = false
        loading.value = false
    }

    fun deleteForecastWeather(uuid: Int?){
        launch {
            val dao = WeatherDatabase(getApplication()).weatherDao()
            uuid?.let {
                val forecastWeather = dao.getForecastWeather(it)
                dao.deleteForecastWeatherById(it)

                if(forecastWeather.active == true){
                    dao.setActiveLastForecastWeather()
                }
            }
        }
    }

    fun setActiveForecastWeather(uuid: Int?){
        launch {
            val dao = WeatherDatabase(getApplication()).weatherDao()
            uuid?.let {
                dao.updateForecastWeatherActive()
                dao.setActiveForecastWeatherById(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}
package com.marketkhoone.weather.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.marketkhoone.weather.R
import com.marketkhoone.weather.model.entity.DayMode
import com.marketkhoone.weather.util.DividerItemDecorator
import com.marketkhoone.weather.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), ForecastWeathersListAdapter.DeleteListener {

    private lateinit var viewModel: HomeViewModel
    private val forecastWeathersListAdapter = ForecastWeathersListAdapter(arrayListOf(), this)
    private val accessToken = "pk.eyJ1IjoibWV5c2FtZmFybWFuaSIsImEiOiJjazE2MzNvazcwN3NnM2lvMm5rdzdkcDU4In0.74J9-_k2j7uwcnXKGINiyg"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).changeStatusBarColor(DayMode.Day)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.fetchList()

        forecastWeatherList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = forecastWeathersListAdapter
            val dividerItemDecoration: RecyclerView.ItemDecoration =
                DividerItemDecorator(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.item_divider
                    )!!
                )
            forecastWeatherList.addItemDecoration(dividerItemDecoration)
        }

        var itemTouchHelper = ItemTouchHelper(SwipeToDelete(forecastWeathersListAdapter))
        itemTouchHelper.attachToRecyclerView(forecastWeatherList)

        floatingActionButton.setOnClickListener {
            val intent = PlaceAutocomplete.IntentBuilder()
                .accessToken(accessToken)
                .placeOptions(
                    PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#fbfbfb"))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS)
                )
                .build(activity)
            startActivityForResult(intent, 100)
        }

        homeRefreshLayout.setOnRefreshListener {
            forecastWeatherList.visibility = View.GONE
            listError.visibility = View.GONE
            homeLoadingView.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            homeRefreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            val feature = PlaceAutocomplete.getPlace(data)
            val geometry = (feature.geometry() as Point)
            val placeName = feature.placeName()
            val city = feature.text()
            val country = placeName?.split(", ")?.last()
            geometry?.let {
                viewModel.fetchFromRemote(
                    geometry.latitude().toString(),
                    geometry.longitude().toString(),
                    city,
                    country
                )
            }

        }
    }

    fun observeViewModel(){
        viewModel.forecastWeathers.observe(viewLifecycleOwner, Observer { forecastWeathers ->
            forecastWeathers?.let {
                forecastWeatherList.visibility = View.VISIBLE
                if(forecastWeathers.size > 0){
                    emptyList.visibility = View.GONE
                }else{
                    emptyList.visibility = View.VISIBLE
                }
                forecastWeathersListAdapter.updateForecastWeathersList(forecastWeathers)
            }
        })

        viewModel.forecastWeathersLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                homeLoadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    forecastWeatherList.visibility = View.GONE
                    emptyList.visibility = View.GONE
                }
            }
        })
    }

    override fun onDeleteListener(uuid: Int?) {
        viewModel.deleteForecastWeather(uuid)
    }

    override fun onClickListener(uuid: Int?) {
        viewModel.setActiveForecastWeather(uuid)
        (activity as MainActivity).performExploreClick()
    }
}
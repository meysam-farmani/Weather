package com.marketkhoone.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.marketkhoone.weather.R
import com.marketkhoone.weather.databinding.FragmentForecastBinding
import com.marketkhoone.weather.model.entity.Daily
import com.marketkhoone.weather.model.entity.DayMode
import com.marketkhoone.weather.util.DividerItemDecorator
import com.marketkhoone.weather.viewmodel.ForecastViewModel
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.collections.emptyList


class ForecastFragment : Fragment() {

    private lateinit var viewModel: ForecastViewModel
    private lateinit var dataBinding: FragmentForecastBinding
    private val forecastListAdapter = ForecastListAdapter(arrayListOf())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_forecast, container, false)
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_forecast,
            container,
            false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ForecastViewModel::class.java)
        viewModel.fetch()

        (activity as MainActivity).changeStatusBarColor(DayMode.Day)

        forecastList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = forecastListAdapter
            val dividerItemDecoration: ItemDecoration =
                DividerItemDecorator(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.item_divider_dark
                    )!!
                )
            forecastList.addItemDecoration(dividerItemDecoration)
            setHasFixedSize(true)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.forecastWeatherLiveData.observe(viewLifecycleOwner, Observer { forecastWeather ->
            forecastWeather?.let {

                dataBinding.weather = it
                forecastLayout.visibility = View.VISIBLE
                forecastLoadingView.visibility = View.GONE
                it.daily?.let { daily ->
                    it.daily?.get(0)?.expand = true
                    forecastListAdapter.updateForecastList(daily as List<Daily>)
                }

            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                forecastLoadingView.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }
}
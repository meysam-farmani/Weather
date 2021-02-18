package com.marketkhoone.weather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.marketkhoone.weather.R
import com.marketkhoone.weather.databinding.FragmentExploreBinding
import com.marketkhoone.weather.model.entity.DayMode
import com.marketkhoone.weather.model.entity.Hourly
import com.marketkhoone.weather.viewmodel.ExploreViewModel
import com.marketkhoone.weather.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.fragment_forecast.*
import kotlinx.android.synthetic.main.fragment_home.*

class ExploreFragment : Fragment() {

    private lateinit var viewModel: ExploreViewModel
    private lateinit var dataBinding: FragmentExploreBinding
    private val hourlyListAdapter = HourlyListAdapter(arrayListOf(),"")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_explore,container,false)
        return dataBinding.root
//        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ExploreViewModel::class.java)
        viewModel.fetch()

        hourlyList.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = hourlyListAdapter
        }

        observeViewModel()
    }

    private  fun observeViewModel(){
        viewModel.forecastWeatherLiveData.observe(viewLifecycleOwner, Observer {forecastWeather ->
            forecastWeather?.let {
                dataBinding.weather = it
                exploreLayout.visibility = View.VISIBLE
                exploreLoadingView.visibility = View.GONE

                it.hourly?.let {hourly ->
                    hourlyListAdapter.updateHourlyList(hourly as List<Hourly>, it.timezone)
                }

                setStatusBarColor(it.current?.weather?.get(0)?.icon)
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                exploreLoadingView.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }

    private fun setStatusBarColor(iconName : String?){
        iconName?.let {
            if(it.last() == 'n'){
                (activity as MainActivity).changeStatusBarColor(DayMode.Night)
            }else{
                (activity as MainActivity).changeStatusBarColor(DayMode.Day)
            }
        }
    }
}
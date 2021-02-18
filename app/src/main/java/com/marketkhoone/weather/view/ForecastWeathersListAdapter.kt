package com.marketkhoone.weather.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marketkhoone.weather.R
import com.marketkhoone.weather.databinding.ItemWeatherBinding
import com.marketkhoone.weather.model.entity.ForecastWeather
import kotlinx.android.synthetic.main.item_weather.view.*
import java.text.SimpleDateFormat
import java.util.*

class ForecastWeathersListAdapter(val forecastWeathersList: ArrayList<ForecastWeather>, private val listener: DeleteListener) :
    RecyclerView.Adapter<ForecastWeathersListAdapter.ForecastWeatherViewHolder>(), ForecastWeatherClickListener {
    class ForecastWeatherViewHolder(var view: ItemWeatherBinding) : RecyclerView.ViewHolder(view.root)

    interface DeleteListener {
        fun onDeleteListener(uuid: Int?)
        fun onClickListener(uuid: Int?)
    }

    fun updateForecastWeathersList(newForecastWeathersList: List<ForecastWeather>){
        forecastWeathersList.clear()
        forecastWeathersList.addAll(newForecastWeathersList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastWeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(R.layout.item_forecastWeather, parent, false)
        val view = DataBindingUtil.inflate<ItemWeatherBinding>(
            inflater,
            R.layout.item_weather, parent, false
        )
        return ForecastWeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return forecastWeathersList.size
    }

    override fun onBindViewHolder(holder: ForecastWeatherViewHolder, position: Int) {
        holder.view.weather = forecastWeathersList[position]
        holder.view.listener = this
    }

    override fun onForecastWeatherClicked(v: View) {
        var uuid = v.forecastWeatherId.text.toString().toInt()
        listener.onClickListener(uuid)
    }

    fun deleteItem(position: Int){
        val uuid = forecastWeathersList.get(position).uuid
        listener.onDeleteListener(uuid)
        forecastWeathersList.removeAt(position)
        notifyItemRemoved(position)
    }
}
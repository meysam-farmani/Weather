package com.marketkhoone.weather.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marketkhoone.weather.R
import com.marketkhoone.weather.databinding.ItemForecastBinding
import com.marketkhoone.weather.model.entity.Daily
import java.util.*

class ForecastListAdapter(val forecastList: ArrayList<Daily>) :
    RecyclerView.Adapter<ForecastListAdapter.ForecastViewHolder>() {
    class ForecastViewHolder(var view: ItemForecastBinding) : RecyclerView.ViewHolder(view.root)

    fun updateForecastList(newForecastList: List<Daily>){
        forecastList.clear()
        forecastList.addAll(newForecastList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(R.layout.item_forecastForecast, parent, false)
        val view = DataBindingUtil.inflate<ItemForecastBinding>(
            inflater,
            R.layout.item_forecast, parent, false
        )
        return ForecastViewHolder(view)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        var forecast = forecastList[position]
        holder.view.forecast = forecast

        val expanded: Boolean = forecast.isExpanded()

        holder.view.subLayout.setVisibility(if (expanded) View.VISIBLE else View.GONE)
        holder.view.layout.setBackgroundColor(if (expanded) ContextCompat.getColor(holder.itemView.context, R.color.colorItemExpand) else ContextCompat.getColor(holder.itemView.context, R.color.colorDay))

        holder.itemView.setOnClickListener { v ->
            for(index in 0..(forecastList.size - 1)){
                if (forecastList[index].isExpanded()){
                    forecastList[index].setExpanded(false)
                    notifyItemChanged(index)
                    break
                }
            }

            val expanded: Boolean = forecast.isExpanded()
            forecast.setExpanded(!expanded)
            notifyItemChanged(position)
        }
    }
}
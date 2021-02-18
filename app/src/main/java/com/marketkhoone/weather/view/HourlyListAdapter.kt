package com.marketkhoone.weather.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marketkhoone.weather.R
import com.marketkhoone.weather.databinding.ItemHourlyBinding
import com.marketkhoone.weather.model.entity.Hourly
import java.util.*

class HourlyListAdapter(val hourlyList: ArrayList<Hourly>, var timezone: String?) :
    RecyclerView.Adapter<HourlyListAdapter.HourlyViewHolder>() {
    class HourlyViewHolder(var view: ItemHourlyBinding) : RecyclerView.ViewHolder(view.root)

    fun updateHourlyList(newHourlyList: List<Hourly>, timezone: String?){
        hourlyList.clear()
        hourlyList.addAll(newHourlyList)
        notifyDataSetChanged()
        this.timezone = timezone
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
//        val view = inflater.inflate(R.layout.item_forecastHourly, parent, false)
        val view = DataBindingUtil.inflate<ItemHourlyBinding>(
            inflater,
            R.layout.item_hourly, parent, false
        )
        return HourlyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hourlyList.size
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.view.hourly = hourlyList[position]
        holder.view.timezone = timezone
    }
}
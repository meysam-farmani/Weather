package com.marketkhoone.weather.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.marketkhoone.weather.R
import java.text.SimpleDateFormat
import java.util.*


fun getProgressDrawable(context: Context):CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(iconName: String?, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_weather)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(getImage(iconName, context))
        .into(this)
}

fun getImage(imageName: String?, context: Context): Int {
    return context.resources.getIdentifier(imageName, "drawable", context.getPackageName())
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, iconName: String?){
    view.loadImage("ic_" + iconName, getProgressDrawable(view.context))
}

@BindingAdapter("android:backgroundColor")
fun setBackgroundColor(view: View, iconName: String?){
    iconName?.let {
        if(it.last() == 'n'){
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorNight))
        }else{
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorDay))
        }
    }
}

@BindingAdapter("android:itemBackgroundColor")
fun setItemBackgroundColor(view: View, iconName: String?){
    iconName?.let {
        if(it.last() == 'n'){
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorNight))
        }else{
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorBlueDay))
        }
    }
}

@BindingAdapter("android:hourlyBackgroundColor")
fun setHourlyBackgroundColor(view: View, iconName: String?){
    iconName?.let {
        val gd = GradientDrawable()
        gd.cornerRadius = dpToPx(view.context, 28F)
        gd.setStroke(2, ContextCompat.getColor(view.context, R.color.colorSecondText))
        if(it.last() == 'n'){
            gd.setColor(ContextCompat.getColor(view.context, R.color.colorNight))
        }else{
            gd.setColor(ContextCompat.getColor(view.context, R.color.colorDay))
        }
        view.background = gd
    }
}

@BindingAdapter("android:reverseColor")
fun setTextColor(view: TextView, iconName: String?){
    iconName?.let {
        if(it.last() == 'n'){
            view.setTextColor(ContextCompat.getColor(view.context, R.color.colorDay))
        }else{
            view.setTextColor(ContextCompat.getColor(view.context, R.color.colorNight))
        }
    }
}

@BindingAdapter("android:textTemp")
fun setTextTemp(view: TextView, temp: String?){
    temp?.let {
        val number = it + "f"
        val tempString = String.format("%.0f", number.toFloat()) + "\u00B0"
        view.setText(tempString)
    }
}

@BindingAdapter("android:textHumidity")
fun setTextHumidity(view: TextView, humidity: Int?){
    humidity?.let {
        val humidityString = it.toString() + "%"
        view.setText(humidityString)
    }
}

@BindingAdapter("android:textDateFull")
fun setTextDateFull(view: TextView, dateUnix: Int?){
    dateUnix?.let {
        val date: Date = Date(it.toLong() * 1000L)
        val sdf: SimpleDateFormat = SimpleDateFormat("EEEE dd MMMM yyyy")
        val formattedDate: String = sdf.format(date)
        view.setText(formattedDate)
    }
}

@BindingAdapter("android:unixTime","android:timezone")
fun setTextTime(view: TextView, unixTime: Int?, timezone : String?){
    unixTime?.let {
        val date: Date = Date(it.toLong() * 1000L)
        val sdf: SimpleDateFormat = SimpleDateFormat("K a")
        sdf.timeZone = TimeZone.getTimeZone(timezone)
        val formattedDate: String = sdf.format(date)
        view.setText(formattedDate)
    }
}

@BindingAdapter("android:textTimeFull")
fun setTextTimeFull(view: TextView, dateUnix: Int?){
    dateUnix?.let {
        val date: Date = Date(it.toLong() * 1000L)
        val sdf: SimpleDateFormat = SimpleDateFormat("hh:mm a")
        val formattedDate: String = sdf.format(date)
        view.setText(formattedDate)
    }
}

@BindingAdapter("android:textDateShort")
fun setTextDateShort(view: TextView, dateUnix: Int?){
    dateUnix?.let {
        val date: Date = Date(it.toLong() * 1000L)
        val sdf: SimpleDateFormat = SimpleDateFormat("EEEE, dd MMMM")
        val formattedDate: String = sdf.format(date)
        view.setText(formattedDate)
    }
}

@BindingAdapter("android:convertSpeed")
fun convertSpeed(view: TextView, speed: String?){
    speed?.let {
        val number = it + "f"
        val kilometerPerHour = String.format("%.0f", (number.toFloat() * (60 * 60)) / 1000) + " km/h"
        view.setText(kilometerPerHour)
    }
}

fun dpToPx(context: Context, dp: Float): Float {
    return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

class SimpleDividerItemDecoration(context: Context?) :
    ItemDecoration() {
    private val mDivider: Drawable?
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.adapter!!.itemCount
        for (i in 0 until childCount) {
            if (i == childCount - 1) {
                continue
            }
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    init {
        mDivider = ContextCompat.getDrawable(context!!, R.drawable.item_divider_dark)
    }
}
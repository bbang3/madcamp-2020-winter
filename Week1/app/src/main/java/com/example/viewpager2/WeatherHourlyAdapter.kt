package com.example.viewpager2

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherHourlyAdapter(private val weatherHourlyList: ArrayList<Weather>) :
    RecyclerView.Adapter<WeatherHourlyAdapter.WeatherHourlyViewHolder>() {

    class WeatherHourlyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var hourlyTime: TextView = itemView.findViewById(R.id.hourly_time)
        var hourlyWeatherIcon: ImageView = itemView.findViewById(R.id.hourly_weather_icon)
        var hourlyTemp: TextView = itemView.findViewById(R.id.hourly_temp)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherHourlyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_hourly_item, parent, false)
        return WeatherHourlyViewHolder(view)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: WeatherHourlyViewHolder, position: Int) {
        var currentItem = weatherHourlyList[position]

        holder.hourlyTime.text = currentItem.date
        holder.hourlyWeatherIcon.setImageResource(currentItem.weatherIcon)
        holder.hourlyTemp.text = "${currentItem.temp}°C"
    }

    override fun getItemCount(): Int {
        Log.i("aaaa", weatherHourlyList.size.toString())
        return weatherHourlyList.size
    }

}

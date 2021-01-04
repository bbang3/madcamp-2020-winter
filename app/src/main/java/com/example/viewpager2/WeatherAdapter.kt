package com.example.viewpager2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter(private val weatherList: ArrayList<Weather>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherAdapter.WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: WeatherAdapter.WeatherViewHolder, position: Int) {
        var currentItem = weatherList[position]
        holder.date.text = currentItem.date
        holder.status.text = currentItem.status
        holder.temp.text = currentItem.temp
//        holder.weatherIcon.srcCompat = currentItem.weatherIcon
        holder.card.setOnClickListener {
            val intent: Intent = Intent(holder.card.context, WeatherDetailActivity::class.java)
            holder.card.context.startActivity(intent)
        }
    }

    class WeatherViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val status: TextView = itemView.findViewById(R.id.status)
        val temp: TextView = itemView.findViewById(R.id.temp)
        val weatherIcon: Image = itemView.findViewById(R.id.weatherIcon)
        val card: CardView = itemView.findViewById(R.id.weather_card)
    }


}

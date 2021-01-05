package com.example.viewpager2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.security.AccessController.getContext

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
        holder.statusIcon.setImageResource(currentItem.weatherIcon)
        holder.card.setOnClickListener {
            val intent: Intent = Intent(holder.card.context, WeatherDetailActivity::class.java)
            intent.putExtra("index", position)
            holder.card.context.startActivity(intent)
        }
    }
    class WeatherViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.date)
        var status: TextView = itemView.findViewById(R.id.status)
        var temp: TextView = itemView.findViewById(R.id.temp)
        var statusIcon: ImageView = itemView.findViewById(R.id.statusIcon)
        var card: CardView = itemView.findViewById(R.id.weather_card)
    }
}

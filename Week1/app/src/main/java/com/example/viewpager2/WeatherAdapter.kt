package com.example.viewpager2

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter(private val weatherList: ArrayList<Weather>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.today_date)
        var status: TextView = itemView.findViewById(R.id.today_weather)
        var temp: TextView = itemView.findViewById(R.id.temp)
        var statusIcon: ImageView = itemView.findViewById(R.id.statusIcon)
        var card: CardView = itemView.findViewById(R.id.weather_card)
        var tempColor : LinearLayout = itemView.findViewById(R.id.layout1)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(view)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        var currentItem = weatherList[position]
        holder.date.text = currentItem.date
        holder.status.text = currentItem.status
        holder.temp.text = "${currentItem.temp}°C"
        Log.i("tempcolor",currentItem.tempColor.toString())
        holder.tempColor.setBackgroundColor(currentItem.tempColor)
        holder.statusIcon.setImageResource(currentItem.weatherIcon)
        holder.card.setOnClickListener {
            val intent: Intent = Intent(holder.card.context, WeatherDetailActivity::class.java)
            intent.putExtra("index", position)
            holder.card.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        Log.i("aaaa",weatherList.size.toString())
        return weatherList.size
    }

}

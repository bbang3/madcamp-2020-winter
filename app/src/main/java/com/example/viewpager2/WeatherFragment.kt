package com.example.viewpager2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.activity_weather_detail.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.security.AccessController
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class WeatherFragment : Fragment() {
    lateinit var Tomorrow: Weather
    lateinit var OneDayAfter: Weather
    lateinit var adapter: WeatherAdapter
    lateinit var Day1 : Weather
    lateinit var Day2 : Weather
    lateinit var Day3 : Weather
    lateinit var Day4 : Weather
    lateinit var Day5 : Weather

    var weatherList: ArrayList<Weather> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        weatherTask().execute()
        Log.i("bbbb","${weatherList.size}")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather, container, false)
        var rvWeather = view.findViewById<RecyclerView>(R.id.rv_weather)

        adapter = WeatherAdapter(weatherList)
        rvWeather.adapter = adapter
        rvWeather.layoutManager = LinearLayoutManager(activity)
        rvWeather.setHasFixedSize(true)
        Log.i("aaaa","oncreateview a")
        return view
    }


    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/onecall?lat=36.33&lon=127.42&appid=828226bad003adf44e43727a145b1fa4&exclude=hourly,minutely&units=metric").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        @SuppressLint("NewApi")
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                class WeatherInfo(var position: Int){
                    var day =jsonObj.getJSONArray("daily").getJSONObject(position)
                    var weatherObj = day.getJSONArray("weather").getJSONObject(0)
                    var weatherDisc = weatherObj.getString("main")
                    var date = SimpleDateFormat(
                        "yyyy.MM.dd",
                        Locale.KOREAN
                    ).format(Date(day.getLong("dt") * 1000))
                    var temp = day.getJSONObject("temp").getString("day") + "°C"
                    var icon: Int = when(weatherDisc){
                        "Clear"->  R.drawable.clear_sky
                        "Clouds" -> R.drawable.clouds
                        "Snow" -> R.drawable.snow
                        "Thunderstorm" -> R.drawable.thunderstorm
                        "Drizzle" -> R.drawable.rain
                        "Rain" -> R.drawable.rain
                        else -> R.drawable.clear_sky
                    }
                    var tempNum : Float = temp.substring(0,temp.length-2).toFloat()
                    var red: Float = ((tempNum+15)*8.5).toFloat()
                    var blue: Float = ((15-tempNum)*8.5).toFloat()
                    var green: Float = 0.toFloat()
                    var tempColor = Color.rgb(red,green,blue)
                }
                var Obj1 = WeatherInfo(1)
                Day1 = Weather(Obj1.date,Obj1.weatherDisc,Obj1.icon,Obj1.temp,Obj1.tempColor)
                var Obj2 = WeatherInfo(2)
                Day2 = Weather(Obj2.date,Obj2.weatherDisc,Obj2.icon,Obj2.temp,Obj2.tempColor)
                var Obj3 = WeatherInfo(3)
                Day3 = Weather(Obj3.date,Obj3.weatherDisc,Obj3.icon,Obj3.temp,Obj3.tempColor)
                var Obj4 = WeatherInfo(4)
                Day4 = Weather(Obj4.date,Obj4.weatherDisc,Obj4.icon,Obj4.temp,Obj4.tempColor)
                var Obj5 = WeatherInfo(5)
                Day5 = Weather(Obj5.date,Obj5.weatherDisc,Obj5.icon,Obj5.temp,Obj5.tempColor)
                weatherList.addAll(arrayListOf(Day1,Day2, Day3, Day4, Day5))
                adapter.notifyDataSetChanged()


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}



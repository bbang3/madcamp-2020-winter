package com.example.viewpager2

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_weather_detail.*
import kotlinx.android.synthetic.main.activity_weather_detail.loader
import kotlinx.android.synthetic.main.fragment_weather.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class WeatherFragment : Fragment() {
    lateinit var adapter: WeatherAdapter
    lateinit var hourlyAdapter: WeatherHourlyAdapter

    var weatherList: ArrayList<Weather> = ArrayList()
    var weatherHourlyList: ArrayList<Weather> = ArrayList()

    val CITY: String = "daejeon"
    val LOC: Pair<Double, Double> = Pair(36.33, 127.42)
    val API: String ="828226bad003adf44e43727a145b1fa4"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        weatherTask().execute()
        WeatherHourlyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

        var rvWeather = view.findViewById<RecyclerView>(R.id.rv_weather)
        adapter = WeatherAdapter(weatherList)
        rvWeather.adapter = adapter
        rvWeather.layoutManager = LinearLayoutManager(activity)
        rvWeather.setHasFixedSize(true)

        var rvWeatherHourly = view.findViewById<RecyclerView>(R.id.rv_hourly)
        hourlyAdapter = WeatherHourlyAdapter(weatherHourlyList)
        rvWeatherHourly.adapter = hourlyAdapter
        rvWeatherHourly.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvWeatherHourly.setHasFixedSize(true)
        rvWeatherHourly

        return view
    }


    inner class weatherTask() : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/onecall?lat=${LOC.first}&lon=${LOC.second}&appid=$API&exclude=minutely,hourly&units=metric").readText(
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
                val timeZoneOffset:Long = jsonObj.getLong("timezone_offset")

                val current = jsonObj.getJSONObject("current")
                val dt =  current.getLong("dt")
                val updatedDate = SimpleDateFormat("yyyy.MM.dd a hh:mm ", Locale.US).format(Date((dt + timeZoneOffset) * 1000))

                val todayTemp = current.getInt("temp")
                val todayWeather = current.getJSONArray("weather").getJSONObject(0)
                val todayWeatherMain = todayWeather.getString("main")
                val todayWeatherDisc = todayWeather.getString("description")

                val todayIcon: Int = when (todayWeatherMain) {
                    "Clear" -> R.drawable.clear_sky
                    "Clouds" -> R.drawable.clouds
                    "Snow" -> R.drawable.snow
                    "Thunderstorm" -> R.drawable.thunderstorm
                    "Drizzle" -> R.drawable.rain
                    "Rain" -> R.drawable.rain
                    else -> R.drawable.clear_sky
                }
                activity?.findViewById<TextView>(R.id.today_date)?.text = "Updated at: $updatedDate"
                activity?.findViewById<TextView>(R.id.today_weather)?.text = todayWeatherDisc.capitalize()
                activity?.findViewById<ImageView>(R.id.today_statusIcon)?.setImageResource(todayIcon)
                activity?.findViewById<TextView>(R.id.today_temp)?.text = "${todayTemp}°C"

                for (i in 1..5) {
                    var weatherInfo = WeatherInfo(jsonObj, i)
                    val weather = Weather(
                        weatherInfo.date,
                        weatherInfo.weatherDisc,
                        weatherInfo.icon,
                        weatherInfo.temp,
                        weatherInfo.tempColor
                    )

                    weatherList.add(weather)
                }

                adapter.notifyDataSetChanged()
                hourlyAdapter.notifyDataSetChanged()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class WeatherHourlyTask() : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/onecall?lat=${LOC.first}&lon=${LOC.second}&appid=$API&exclude=minutely,daily&units=metric").readText(
                    Charsets.UTF_8
                )
            } catch (e: Exception) {
                e.printStackTrace()
                response = null
            }
            return response
        }

        @SuppressLint("NewApi")
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val timeZoneOffset:Long = jsonObj.getLong("timezone_offset")
                val hourlyArray = jsonObj.getJSONArray("hourly")

                for (i in 0..23) {
                    val currentHourObj = hourlyArray.getJSONObject(i)

                    val dt = currentHourObj.getLong("dt")
                    val date = SimpleDateFormat("hh a", Locale.US).format(Date((dt + timeZoneOffset) * 1000))
                    val weather = currentHourObj.getJSONArray("weather").getJSONObject(0).getString("main")
                    val temp = currentHourObj.getDouble("temp").roundToInt()

                    val weatherIcon:Int = when(weather) {
                        "Clear" -> R.drawable.clear_sky
                        "Clouds" -> R.drawable.clouds
                        "Snow" -> R.drawable.snow
                        "Thunderstorm" -> R.drawable.thunderstorm
                        "Drizzle" -> R.drawable.rain
                        "Rain" -> R.drawable.rain
                        else -> R.drawable.clear_sky
                    }

                    weatherHourlyList.add(Weather(date, "", weatherIcon, temp, 0))
                }
                adapter.notifyDataSetChanged()
                hourlyAdapter.notifyDataSetChanged()


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class WeatherInfo(val jsonObj: JSONObject, var position: Int) {
    var day = jsonObj.getJSONArray("daily").getJSONObject(position)
    var weatherObj = day.getJSONArray("weather").getJSONObject(0)
    var weatherDisc = weatherObj.getString("main")
    var date = SimpleDateFormat(
        "MM.dd",
        Locale.KOREAN
    ).format(Date(day.getLong("dt") * 1000))
    var temp = day.getJSONObject("temp").getDouble("day").roundToInt()
    var icon: Int = when (weatherDisc) {
        "Clear" -> R.drawable.clear_sky
        "Clouds" -> R.drawable.clouds
        "Snow" -> R.drawable.snow
        "Thunderstorm" -> R.drawable.thunderstorm
        "Drizzle" -> R.drawable.rain
        "Rain" -> R.drawable.rain
        else -> R.drawable.clear_sky
    }
    var tempNum: Float = day.getJSONObject("temp").getString("day").toFloat()
    var red: Float = ((tempNum + 15) * 8.5).toFloat()
    var blue: Float = ((15 - tempNum) * 8.5).toFloat()
    var green: Float = 0.toFloat()
    @RequiresApi(Build.VERSION_CODES.O)
    var tempColor = Color.rgb(red, green, blue)
}

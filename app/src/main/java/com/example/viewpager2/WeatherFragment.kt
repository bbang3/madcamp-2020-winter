package com.example.viewpager2

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
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
    lateinit var Today: Weather
    lateinit var Tomorrow: Weather
    lateinit var OneDayAfter: Weather
    lateinit var adapter: WeatherAdapter
    var weatherList: ArrayList<Weather> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("aaaa","weatherTask b")
//        weatherTask().execute()
        Log.i("aaaa","weatherTask a")
    }

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

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        weatherTask().execute()
//    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
//            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
//            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
//            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/onecall?lat=36.33&lon=127.42&appid=828226bad003adf44e43727a145b1fa4&exclude=hourly,minutely").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val today = jsonObj.getJSONObject("current")
                val todayWeather = today.getJSONArray("weather").getJSONObject(0)
                val todayWeatherDiscription = todayWeather.getString("main")
                val todayUpdatedAt: Long = today.getLong("dt")
                val todayUpdatedAtText = SimpleDateFormat(
                    "yyyy.MM.dd",
                    Locale.KOREAN
                ).format(Date(todayUpdatedAt * 1000))
                val todayTemp = today.getString("temp") + "°C"
//                val todayIcon = todayWeather.getString("icon")

                val tomorrow = jsonObj.getJSONArray("daily").getJSONObject(0)
                val tomorrowWeather = tomorrow.getJSONArray("weather").getJSONObject(0)
                val tomorrowWeatherDiscription = tomorrowWeather.getString("main")
                val tomorrowUpdatedAt: Long = tomorrow.getLong("dt")
                val tomorrowUpdatedAtText = SimpleDateFormat(
                    "yyyy.MM.dd",
                    Locale.KOREAN
                ).format(Date(tomorrowUpdatedAt * 1000))
                val tomorrowTemp = tomorrow.getString("temp") + "°C"
//                val tomorrowIcon = tomorrowWeather.getString("icon")

                val onedayafter = jsonObj.getJSONArray("daily").getJSONObject(1)
                val onedayafterWeather = onedayafter.getJSONArray("weather").getJSONObject(0)
                val onedayafterWeatherDiscription = onedayafterWeather.getString("main")
                val onedayafterUpdatedAt: Long = onedayafter.getLong("dt")
                val onedayafterUpdatedAtText = SimpleDateFormat(
                    "yyyy.MM.dd",
                    Locale.KOREAN
                ).format(Date(onedayafterUpdatedAt * 1000))
                val onedayafterTemp = onedayafter.getString("temp") + "°C"
//                val onedayafterIcon = onedayafterWeather.getString("icon")


                var todayIcon: Int = when(todayWeatherDiscription){
                    "Clear"->  R.drawable.clear_sky
                    "Clouds" -> R.drawable.clouds
                    "Snow" -> R.drawable.snow
                    "Thunderstorm" -> R.drawable.thunderstorm
                    "Drizzle" -> R.drawable.rain
                    "Rain" -> R.drawable.rain
                    else -> R.drawable.clear_sky
                }
//                var todayIconUri : Uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context?.resources?.getResourcePackageName(todayIcon) + '/' + context?.resources.getResourceTypeName(todayIcon) + '/' + context?.resources?.getResourceEntryName(todayIcon))

                Today = Weather(todayUpdatedAtText,todayWeatherDiscription,todayIcon,todayTemp)
                Log.i("aaaa","today"+todayUpdatedAtText+"\n"+todayWeatherDiscription+"\n"+todayTemp)

                lateinit var tomorrowUri : Uri
                var tomorrowIcon: Int = when(tomorrowWeatherDiscription){
                    "Clear"->  R.drawable.clear_sky
                    "Clouds" -> R.drawable.clouds
                    "Snow" -> R.drawable.snow
                    "Thunderstorm" -> R.drawable.thunderstorm
                    "Drizzle" -> R.drawable.rain
                    "Rain" -> R.drawable.rain
                    else -> R.drawable.clear_sky
                }
                Tomorrow = Weather(tomorrowUpdatedAtText,tomorrowWeatherDiscription,tomorrowIcon,tomorrowTemp)

                 var onedayafterUri : Uri
                var onedayafterIcon : Int = when(onedayafterWeatherDiscription){
                    "Clear"->  R.drawable.clear_sky
                    "Clouds" -> R.drawable.clouds
                    "Snow" -> R.drawable.snow
                    "Thunderstorm" -> R.drawable.thunderstorm
                    "Drizzle" -> R.drawable.rain
                    "Rain" -> R.drawable.rain
                    else -> R.drawable.clear_sky
                }
                OneDayAfter = Weather(onedayafterUpdatedAtText,onedayafterWeatherDiscription,onedayafterIcon,onedayafterTemp)

                weatherList.addAll(arrayListOf(Today, Tomorrow, OneDayAfter))
                adapter.notifyDataSetChanged()
                Log.i("WeatherFragment", "${weatherList.size}")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}



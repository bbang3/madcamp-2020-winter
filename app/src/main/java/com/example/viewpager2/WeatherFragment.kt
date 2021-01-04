package com.example.viewpager2

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.activity_weather_detail.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class WeatherFragment : Fragment() {
    private var weatherList: ArrayList<String> = arrayListOf("Card a", "Card b", "Card c")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        var rvWeather = view.findViewById<RecyclerView>(R.id.rv_weather)
        rvWeather.adapter = WeatherAdapter(weatherList)
        rvWeather.layoutManager = LinearLayoutManager(activity)
        rvWeather.setHasFixedSize(true)

        return view
    }


//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        weatherTask().execute()
//    }
//    inner class weatherTask(): AsyncTask<String, Void, String>(){
//        override fun onPreExecute(){
//            super.onPreExecute()
//            loader.visibility = View.VISIBLE
//            mainContainer.visibility = View.GONE
//            errorText.visibility = View.GONE
//        }
//
//        override fun doInBackground(vararg p0: String?): String? {
//            var response: String?
//            try{
//                response = URL("http://api.openweathermap.org/data/2.5/weather?lat=36.374464110007466&lon=127.36551889624869&APPID=828226bad003adf44e43727a145b1fa4").readText(Charsets.UTF_8)
//            }
//            catch (e: Exception){
//                response = null
//            }
//            return response
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//            try{
//                val jsonObj = JSONObject(result)
//                val main = jsonObj.getJSONObject("main")
//                val sys = jsonObj.getJSONObject("sys")
//                val wind = jsonObj.getJSONObject("wind")
//                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
//                val updatedAt: Long = jsonObj.getLong("dt")
//                val updatedAtText  = "Updated at: "+ SimpleDateFormat("yyyy.MM.dd hh:mm a", Locale.KOREAN).format(
//                    Date(updatedAt*1000)
//                )
//                val temp = main.getString("temp")+"°C"
//                val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
//                val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
//                val pressure = main.getString("pressure")
//                val humidity = main.getString("humidity")
//
//                val sunrise:Long = sys.getLong("sunrise")
//                val sunset:Long = sys.getLong("sunset")
//                val windSpeed = wind.getString("speed")
//                val weatherDescription = weather.getString("description")
//
//                val address = jsonObj.getString("name")+", "+sys.getString("country")
//                /* Populating extracted data into our views */
//                Log.i("aaaa","1")
//                address.text = address
//                Log.i("aaaa","2")
//                updated_at.text =  updatedAtText
//                status.text = weatherDescription.capitalize()
//                temp.text = temp
//                temp_min.text = tempMin
//                temp_max.text = tempMax
//                sunrise.text = SimpleDateFormat("hh:mm a", Locale.KOREAN).format(
//                    Date(sunrise*1000)
//                )
//                sunset.text = SimpleDateFormat("hh:mm a", Locale.KOREAN).format(
//                    Date(sunset*1000)
//                )
//                wind.text = windSpeed
//                pressure.text = pressure
//                humidity.text = humidity
//
//                /* Views populated, Hiding the loader, Showing the main design */
//                loader.visibility = View.GONE
//                mainContainer.visibility = View.VISIBLE
//            }
//            catch (e: Exception) {
//                loader.visibility = View.GONE
//                errorText.visibility = View.VISIBLE
//            }
//        }
//    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        weatherbutton.setOnClickListener {
//            val intent = Intent( activity, WeatherDetail::class.java)
//            startActivity(intent)
//        }
//    }

}
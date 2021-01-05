@file:Suppress("DEPRECATION")

package com.example.viewpager2

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_weather_detail.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


@Suppress("DEPRECATION")
class WeatherDetailActivity : AppCompatActivity() {

    val CITY: String = "daejeon"
    val LOC: Pair<Double, Double> = Pair(36.33, 127.42)
    val API: String ="828226bad003adf44e43727a145b1fa4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)
        supportActionBar?.hide()

        val bundle: Bundle? = intent.extras
        val weatherIndex: Int? = bundle?.getInt("index")

        if (weatherIndex != null) {
            WeatherTask(weatherIndex).execute()
        }
        else {
            Toast.makeText(this, "Invalid access", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    inner class WeatherTask(val weatherIndex: Int) : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            mainContainer.visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/onecall?lat=${LOC.first}&lon=${LOC.second}&appid=$API&exclude=minutely,hourly&units=metric").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val currentDay:JSONObject = when(weatherIndex){
                    0 -> jsonObj.getJSONObject("current")
                    else -> jsonObj.getJSONArray("daily").getJSONObject(weatherIndex)
                }
                val timeZoneOffset:Long = jsonObj.getLong("timezone_offset")

                val dt:Long = currentDay.getLong("dt")
                val updatedDate = SimpleDateFormat("yyyy.MM.dd a hh:mm ", Locale.US).format(Date((dt + timeZoneOffset) * 1000))
                val weather:JSONObject = currentDay.getJSONArray("weather").getJSONObject(0)
                val weatherDescription = weather.getString("description")
                val weatherId = weather.getInt("id")
                val weatherIconId = weather.getString("icon")

                val currentTemp:Int
                val tempFeelsLike:Int
                if(weatherIndex == 0) {
                    currentTemp = currentDay.getDouble("temp").roundToInt()
                    tempFeelsLike = currentDay.getDouble("feels_like").roundToInt()
                } else {
                    currentTemp = currentDay.getJSONObject("temp").getDouble("day").roundToInt()
                    tempFeelsLike = currentDay.getJSONObject("feels_like").getDouble("day").roundToInt()
                }

                val tempMax:Int = jsonObj.getJSONArray("daily").getJSONObject(weatherIndex).getJSONObject("temp").getDouble("max").roundToInt()
                val tempMin:Int = jsonObj.getJSONArray("daily").getJSONObject(weatherIndex).getJSONObject("temp").getDouble("min").roundToInt()

                val sunrise:Long = currentDay.getLong("sunrise")
                val sunset:Long = currentDay.getLong("sunset")

                val humidity:Int = currentDay.getInt("humidity")
                val pop:Int = (jsonObj.getJSONArray("daily").getJSONObject(weatherIndex).getDouble("pop") * 100).toInt()
                val windSpeed:Double = currentDay.getDouble("wind_speed")
                val pressure:Int = currentDay.getInt("pressure")

                val address:String = CITY.capitalize()

                /* Populating extracted data into our views */
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = when(weatherIndex) { 0 -> "Updated at: $updatedDate" else -> "Forecast for: $updatedDate"}

                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()

                // setting icon
                val weatherIconUri:String = "icon_$weatherIconId"
                val iconImageResource = resources.getIdentifier(weatherIconUri, "drawable", packageName) //get image  resource
                findViewById<ImageView>(R.id.weather_image).setImageResource(iconImageResource) // set as image

                // setting background image
                val weatherBackgroundUri:String = when(weatherId) {
                    in 200..299 -> "background_storm"
                    in 300..399 -> "background_rain"
                    in 600..699 -> "background_snow"
                    in 500..599 -> "background_rain"
                    701 -> "background_mist"
                    in 801..899 -> "background_cloudy"
                    else -> "background_sunny"
                }

                val weatherBackgroundResource = resources.getIdentifier(weatherBackgroundUri, "drawable",packageName)
                findViewById<RelativeLayout>(R.id.wrapper).setBackgroundResource(weatherBackgroundResource)


                findViewById<TextView>(R.id.temp).text = "${currentTemp}°C"
                findViewById<TextView>(R.id.temp_min).text = "$tempMin°C"
                findViewById<TextView>(R.id.temp_max).text = "$tempMax°C"
                findViewById<TextView>(R.id.temp_feels_like).text = "Feels like: $tempFeelsLike°C"

                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("a hh:mm", Locale.US).format(
                    Date(
                        (sunrise + timeZoneOffset) * 1000
                    )
                )
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("a hh:mm", Locale.US).format(
                    Date(
                        (sunset + timeZoneOffset) * 1000
                    )
                )
                findViewById<TextView>(R.id.humidity).text = "${humidity}%"
                findViewById<TextView>(R.id.pop).text = "${pop}%"
                findViewById<TextView>(R.id.wind).text = "${windSpeed}m/s"
                findViewById<TextView>(R.id.pressure).text = "${pressure}hPa"

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<ConstraintLayout>(R.id.mainContainer).visibility = View.VISIBLE

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
                e.printStackTrace()
            }
        }
    }
}
package com.aicontent.openweather

import android.content.Context
import android.provider.Settings.System.getString
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aicontent.openweather.adapter.BottomAdapter
import com.aicontent.openweather.data.RetrofitInstance
import com.aicontent.openweather.databinding.BottomSheetLayoutBinding
import com.aicontent.openweather.databinding.FragmentCityWeatherBinding
import com.aicontent.openweather.model.forecast.ForecastData
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CityWeatherViewModel : ViewModel() {

    @OptIn(DelicateCoroutinesApi::class)
    fun getCurrentWeather(
        context: Context, // Pass the context as a parameter
        binding: FragmentCityWeatherBinding,
        city: String,
        units: String = context.getString(R.string.metric),
        apiKey: String = context.getString(R.string.api_key)
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getCurrentWeather(
                    city = city,
                    units = units,
                    apiKey = apiKey
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!
                        binding.apply {
                            val iconId = data.weather[0].icon
                            val imageURL = "https://openweathermap.org/img/w/$iconId.png"
                            Log.d("image", iconId)

                            Picasso.get().load(imageURL).into(imgWeather)

                            tvSunset.text = convertTimestampToTime(data.sys.sunset.toLong())
                            tvSunrise.text = convertTimestampToTime(data.sys.sunrise.toLong())
                            tvWind.text = "${data.wind.speed} m/s"
                            tvLocation.text = "${data.name}" + " ${data.sys.country}"
                            tvPressure.text = "${data.main.pressure} hPa"
                            tvHumidity.text = "${data.main.humidity}%"
                            tvUpdateTime.text =
                                "Update at ${convertTimestampToTime(data.dt.toLong())}"
                            tvTemp.text = "${data.main.temp.toInt()}°C"
                            tvMaxTemp.text = "Max ${data.main.temp_max}°C"
                            tvMinTemp.text = "Min ${data.main.temp_min}°C"
                            tvFeelsLike.text = "Feel like ${data.main.feels_like}°C"
                            tvStatus.text = data.weather[0].description

//                            Log.d(
//                                "sunrise and sunset",
//                                "${data.sys.sunrise} and ${data.sys.sunset} "
//                            )
                            getPollution(
                                data.coord.lat,
                                data.coord.lon,
                                binding,
                                units,
                                apiKey,
                                context
                            )
                        }
                    } else {
                        Log.d("HTTP Error", "Response not successful: ${response.code()}")
                        Toast.makeText(
                            context,
                            "HTTP Error: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: IOException) {
                Log.e("IO error", e.toString())
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "IO Error : ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getPollution(
        lat: Double,
        lon: Double,
        binding: FragmentCityWeatherBinding,
        units: String,
        apiKey: String,
        context : Context
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getAirPollution(
                    lat, lon, units, apiKey
                )
                withContext(Dispatchers.Main){
                    if (response.isSuccessful && response.body() != null){
                        val data = response.body()!!
                        if (data.list.isNotEmpty()) {
                            val api = data.list[0].main.aqi
                        binding.tvAirQual.text = when (api) {
                            1 -> "Good"
                            2 -> "Fair"
                            3 -> "Moderate"
                            4 -> "Poor"
                            5 -> "Very poor"
                            else -> "No data"
                        }
                    } else {
                        // Handle the case where the list is empty
                        binding.tvAirQual.text = "No data"
                    }
                    } else {
                        Log.d("HTTP Error", "Response not successful: ${response.code()}")
                        Toast.makeText(
                            context,
                            "HTTP Error: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: IOException) {
                Log.e("IO error", e.toString())
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "IO Error : ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getForecast(
        context: Context, // Pass the context as a parameter
        binding: BottomSheetLayoutBinding,
        city: String,
        units: String = context.getString(R.string.metric),
        apiKey: String = context.getString(R.string.api_key)
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getForecast(
                    city = city,
                    units = units,
                    apiKey = apiKey
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.d("Forecast JSON", response.body().toString())

                        val data = response.body()!!
                        binding.apply {
                            val forecastArray: ArrayList<ForecastData> = data.list as ArrayList<ForecastData>

                            val adapter = BottomAdapter(forecastArray)
                            recyclerViewForecast.adapter = adapter
                            tvSheet.text = "Five days forecast in ${data.city.name}"
                        }
                    } else {
                        Log.d("HTTP Error", "Response not successful: ${response.code()}")
                        Toast.makeText(
                            context,
                            "HTTP Error: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: IOException) {
                Log.e("IO error", e.toString())
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "IO Error : ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun convertTimestampToTime(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        return dateFormat.format(date)
    }
}

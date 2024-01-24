package com.aicontent.openweather.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aicontent.openweather.databinding.RecyclerViewItemLayoutBinding
import com.aicontent.openweather.databinding.RecyclerViewItemSearchHistoryBinding
import com.aicontent.openweather.model.currentWeather.CurrentWeatherModel
import com.aicontent.openweather.model.forecast.ForecastData
import com.aicontent.openweather.model.pollution.PollutionModel
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SearchAdapter(
    private val currentArray: ArrayList<CurrentWeatherModel>,
    private val pollutionArray: ArrayList<PollutionModel>
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    class ViewHolder(val binding: RecyclerViewItemSearchHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerViewItemSearchHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return currentArray.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = currentArray[position]
        val pollutionData = pollutionArray[position]
        holder.binding.apply {
            tvLocation.text = currentItem.name
            tvTemp.text = "${currentItem.main.temp.toInt()}°C"
            tvTime.text = displayTime(currentItem.timezone.toString())
            tvHigh.text = "${currentItem.main.temp_max.toInt()}°C"
            tvLow.text = "${currentItem.main.temp_min.toInt()}°C"
            if (pollutionData.list.isNotEmpty()) {
                val api = pollutionData.list[0].main.aqi
                tvCurrentState.text = when (api) {
                    1 -> "Good"
                    2 -> "Fair"
                    3 -> "Moderate"
                    4 -> "Poor"
                    5 -> "Very poor"
                    else -> "No data"
                }
            }
        }
    }

    private fun displayTime(dtTxt: String): CharSequence? {
        val input =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val output = DateTimeFormatter.ofPattern("MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(dtTxt, input)
        return output.format(dateTime)
    }
}


package com.aicontent.openweather.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aicontent.openweather.databinding.RecyclerViewItemLayoutBinding
import com.aicontent.openweather.databinding.RecyclerViewItemLayoutCountryBinding
import com.aicontent.openweather.model.coutry.CountryItem
import com.aicontent.openweather.model.forecast.ForecastData
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class CountryAdapter(private val countryArray: ArrayList<CountryItem>) :
    RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    class ViewHolder(val binding: RecyclerViewItemLayoutCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerViewItemLayoutCountryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return countryArray.size.toFloat().roundToInt()
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = countryArray[position]
        holder.binding.apply {
            tvItemTime.text = currentItem.name.common
        }
        Log.d("country tag", "what is ${currentItem.name.common}")
    }
}
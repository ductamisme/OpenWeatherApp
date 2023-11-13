package com.aicontent.openweather.model.forecast

data class ForecastModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastData>,
    val message: Int
)
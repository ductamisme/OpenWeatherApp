package com.aicontent.openweather.model.pollution

data class PollutionModel(
    val coord: Coord,
    val list: List<PollutionData>
)
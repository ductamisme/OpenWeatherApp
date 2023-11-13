package com.aicontent.openweather.model.pollution

data class PollutionData (
    val components: Components,
    val dt: Int,
    val main: Main
)
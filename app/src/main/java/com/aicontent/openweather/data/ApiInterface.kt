package com.aicontent.openweather.data

import com.aicontent.openweather.model.coutry.Country
import com.aicontent.openweather.model.coutry.CountryItem
import com.aicontent.openweather.model.currentWeather.CurrentWeatherModel
import com.aicontent.openweather.model.forecast.ForecastModel
import com.aicontent.openweather.model.pollution.PollutionModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("q") city : String,
        @Query("units") units : String,
        @Query("appid") apiKey : String,
    ):Response<CurrentWeatherModel>

    @GET("forecast?")
    suspend fun getForecast(
        @Query ("q") city: String,
        @Query("units") units : String,
        @Query("appid") apiKey : String,
    ) :Response<ForecastModel>

    @GET("air_pollution?")
    suspend fun getAirPollution(
        @Query("lat") lat : Double,
        @Query("lon") lot : Double,
        @Query("units") units : String,
        @Query("appid") apiKey : String,
    ) : Response<PollutionModel>


    @GET("all")
    suspend fun getAllCountry() : Response<CountryItem>
}
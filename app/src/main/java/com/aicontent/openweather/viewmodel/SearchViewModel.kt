package com.aicontent.openweather.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aicontent.openweather.R
import com.aicontent.openweather.adapter.SearchAdapter
import com.aicontent.openweather.data.RetrofitInstance
import com.aicontent.openweather.databinding.FragmentSearchBinding
import com.aicontent.openweather.model.currentWeather.CurrentWeatherModel
import com.aicontent.openweather.model.pollution.PollutionModel
import com.aicontent.openweather.search.SearchEntity
import com.aicontent.openweather.search.SearchHistoryDatabase
import com.aicontent.openweather.search.SearchRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class SearchViewModel(application: Application): AndroidViewModel(application) {
    // Your ViewModel code here
    private lateinit var auth: FirebaseAuth
    private fun updateUI(user: FirebaseUser?) {
    }

    lateinit var readAllData: LiveData<List<SearchEntity>>
    private lateinit var repository: SearchRepository

    init {
        val searchDao = SearchHistoryDatabase.getDatabase(application).searchDao()
        repository = SearchRepository(searchDao)
        readAllData = repository.readAllData
    }

    fun addUser(search: SearchEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(search)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getCurrentWeather(
        applicationContext: Context, // Use applicationContext instead of Context
        binding: FragmentSearchBinding,
        city: String,
        units: String = applicationContext.getString(R.string.metric),
        apiKey: String = applicationContext.getString(R.string.api_key)
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseCurrentWeather = RetrofitInstance.api.getCurrentWeather(
                    city = city,
                    units = units,
                    apiKey = apiKey
                )
                val data = responseCurrentWeather.body()!!
                val responsePollution = RetrofitInstance.api.getAirPollution(
                    data.coord.lat, data.coord.lon, units, apiKey
                )
                withContext(Dispatchers.Main) {
                    handleWeatherResponse(responseCurrentWeather, responsePollution, binding, applicationContext)
                }
            } catch (e: IOException) {
                Log.e("IO error", e.toString())
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        "IO Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun handleWeatherResponse(
        responseCurrentWeather: Response<CurrentWeatherModel>,
        responsePollution: Response<PollutionModel>,
        binding: FragmentSearchBinding,
        context: Context
    ) {
        if (responseCurrentWeather.isSuccessful && responseCurrentWeather.body() != null) {
            if (responsePollution.isSuccessful && responsePollution.body() != null) {
                Log.d("Forecast JSON", responseCurrentWeather.body().toString())

                val dataCurrent = responseCurrentWeather.body()!!
                val dataPollution = responsePollution.body()
                binding.apply {
                    val searchArray: ArrayList<CurrentWeatherModel> =
                        dataCurrent as ArrayList<CurrentWeatherModel>
                    val pollutionArray: ArrayList<PollutionModel> =
                        dataPollution as ArrayList<PollutionModel>
                    val adapter = SearchAdapter(searchArray, pollutionArray)
                    recyclerViewForecast.adapter = adapter
                }
            } else {
                handleErrorResponse(responsePollution.code(), context)
            }
        } else {
            handleErrorResponse(responseCurrentWeather.code(), context)
        }
    }

    private fun handleErrorResponse(errorCode: Int, context: Context) {
        Log.d("HTTP Error", "Response not successful: $errorCode")
        Toast.makeText(
            context,
            "HTTP Error: $errorCode",
            Toast.LENGTH_LONG
        ).show()
    }

}


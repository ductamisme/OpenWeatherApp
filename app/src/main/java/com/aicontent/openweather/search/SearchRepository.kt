package com.aicontent.openweather.search

import androidx.lifecycle.LiveData

class SearchRepository(private val searchDao : SearchDao) {

    val readAllData: LiveData<List<SearchEntity>> = searchDao.readAllData()

    suspend fun addUser(search : SearchEntity){
        searchDao.insertHistorySearch(search)
    }

}
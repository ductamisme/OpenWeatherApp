package com.aicontent.openweather.search

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Delete
    suspend fun deleteHistorySearch(searchHistory: SearchEntity)
    @Upsert
    suspend fun insertHistorySearch(searchHistory: SearchEntity)
    @Query("SELECT * FROM searchEntity ORDER BY name ASC")
    fun getSearchEntityByName(): LiveData<List<SearchEntity>>
}
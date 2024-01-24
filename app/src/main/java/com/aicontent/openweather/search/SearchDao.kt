package com.aicontent.openweather.search

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Delete
    suspend fun deleteHistorySearch(searchHistory: SearchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistorySearch(searchHistory: SearchEntity)

    @Query("SELECT * FROM search_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<SearchEntity>>

    @Query("SELECT * FROM search_table ORDER BY name ASC")
    suspend fun getSearchEntityByName(): LiveData<List<SearchEntity>>
}

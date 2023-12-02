package com.aicontent.openweather.search

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SearchEntity::class],
    version = 1
)
abstract class SearchHistoryDatabase: RoomDatabase() {

    abstract val dao: SearchDao
}
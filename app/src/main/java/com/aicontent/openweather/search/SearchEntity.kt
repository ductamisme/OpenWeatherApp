package com.aicontent.openweather.search

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class SearchEntity(
    val name : String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

)
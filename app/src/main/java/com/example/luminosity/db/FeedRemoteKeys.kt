package com.example.luminosity.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UnsplashDatabase.UNSPLASH_REMOTE_TABLE)
data class FeedRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)

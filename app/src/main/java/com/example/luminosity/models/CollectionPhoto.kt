package com.example.luminosity.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectionPhoto(
    val id: String,
    val title: String,
    @Json(name = "total_photos")
    val totalPhotos: String,
    @Json(name = "cover_photo")
    val coverPhoto:Photo
)
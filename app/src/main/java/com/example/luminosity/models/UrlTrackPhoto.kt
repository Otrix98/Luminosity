package com.example.luminosity.models

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class UrlTrackPhoto(
    val url: String
)
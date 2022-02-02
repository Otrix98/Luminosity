package com.example.luminosity.models

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class UrlsPhoto(
    val thumb: String,
    val small: String,
    val regular: String
)
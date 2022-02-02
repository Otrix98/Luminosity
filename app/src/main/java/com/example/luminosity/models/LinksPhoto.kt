package com.example.luminosity.models
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class LinksPhoto(
    val self: String,
    val html: String,
    val download: String
)
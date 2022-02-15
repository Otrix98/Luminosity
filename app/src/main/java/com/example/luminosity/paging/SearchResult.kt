package com.example.luminosity.paging

import com.example.luminosity.models.Photo
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
@JsonClass(generateAdapter = true)
data class SearchResult(
    val total: Int = 0,
    val results: List<Photo> = emptyList()
)
package com.example.luminosity.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(
    val title: String
)
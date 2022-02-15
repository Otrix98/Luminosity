package com.example.luminosity.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    val city: String? = " - ",
    val country: String? = " - ",
    val position: Position
)
package com.example.luminosity.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
@JsonClass(generateAdapter = true)
data class ProfileImage(
    @SerialName("small")
    @Json(name = "small")
    val profile_small: String,
    val medium: String
)
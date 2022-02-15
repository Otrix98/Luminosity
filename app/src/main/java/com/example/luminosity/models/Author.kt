package com.example.luminosity.models

import androidx.room.Embedded
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class Author(
    val username: String,
    val name: String,
    @Embedded
    val profile_image: ProfileImage,
    val bio: String? = ""
)
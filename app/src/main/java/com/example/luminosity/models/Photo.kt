package com.example.luminosity.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.luminosity.db.UnsplashDatabase
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = UnsplashDatabase.UNSPLASH_TABLE)
@JsonClass(generateAdapter = true)
data class Photo(

    @PrimaryKey
    val id: String,
    val description: String? = "",
    val width: Int,
    val height: Int,
    @Json(name = "alt_description")
    @SerializedName("alt_description")
val altDescription: String? = "",
    @Json(name = "created_at")
    @SerializedName("created_at")
val crated: String,
    val likes: Int,
    @Json(name = "liked_by_user")
    @SerializedName("liked_by_user")
var liked: Boolean,
    @Embedded
    val urls: UrlsPhoto,
    @Json(name = "user")
    @SerializedName("user")
    @Embedded
val author: Author,
    @Embedded
    val links: LinksPhoto
)

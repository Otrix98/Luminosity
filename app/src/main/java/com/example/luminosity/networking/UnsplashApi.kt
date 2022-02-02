package com.example.luminosity.networking

import com.example.luminosity.models.*
import com.squareup.moshi.JsonClass
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface UnsplashApi {


    @GET(value = "/photos")
    suspend fun getListPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<Photo>

    @GET(value = "/collections")
    suspend fun getListCollections(@Query("page") page: Int): List<CollectionPhoto>

    @GET(value = "/me")
    suspend fun getUserProfile(): UserProfile

    @GET(value = "/users/{username}")
    suspend fun getDetailUserProfile(
        @Path("username") userName: String
    ): DetailUserProfile

    @GET(value = "/users/{username}/likes")
    suspend fun getListUsersLikesPhotos(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage:Int = 10
    ): List<Photo>

    @GET(value = "/search/photos")
    suspend fun searchPhotosByKey(
        @Query("query") query: String,
        @Query("per_page") perPage:Int = 10
    ): SearchResult

    @POST(value = "/photos/{id}/like")
    suspend fun postLikeToPhoto(
        @Path("id") id: String,
    ): Response<ResponseBody>

    @DELETE(value = "/photos/{id}/like")
    suspend fun deleteLikeToPhoto(
        @Path("id") id: String,
    ): Response<ResponseBody>

    @GET
    suspend fun getFile(
        @Url url: String
    ): ResponseBody

    @GET(value = "/photos/{id}/download")
    suspend fun trackDownloadPhoto(
        @Path("id") id: String,
    ): UrlTrackPhoto

    @GET
    suspend fun isFileExist(
        @Url url: String
    ): Response<Unit>

    @GET(value = "/collections/{id}/photos")
    suspend fun getListPhotosInCollection(
        @Path("id") id: String,
        @Query("page") page: Int,
    ): List<Photo>

}
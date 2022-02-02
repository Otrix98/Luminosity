package com.example.luminosity.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object Network {


    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(CustomInterceptor())

        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build()

    private  val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://api.unsplash.com")
        .client(okHttpClient)
        .build()

    val unsplashApi: UnsplashApi
        get() = retrofit.create()
}
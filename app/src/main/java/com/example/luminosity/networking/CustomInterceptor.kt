package com.example.luminosity.networking

import com.example.luminosity.data.AccessToken
import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer ${AccessToken.value}")
            .build()

        val request = chain.proceed(modifiedRequest)

        return request
    }
}
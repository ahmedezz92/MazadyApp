package com.example.mazadyapp.core.data.utils

import com.example.mazadyapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
//            .addHeader("Authorization", TOKEN_BEARER)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader("private-key",Constants.Authorization.Private_Key)
            .build()
        return chain.proceed(newRequest)
    }

}
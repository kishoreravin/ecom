package com.example.ecom.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class ApiClient {
    private lateinit var httpClient: OkHttpClient

    private fun getBuilder(url: String): Retrofit.Builder {
        val gson = GsonBuilder().setLenient().disableHtmlEscaping().create()
        return Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson))
    }

    fun getClient(url: String): Retrofit {
        val builder = this.getBuilder(url)
        if (!this::httpClient.isInitialized) {
            this.httpClient = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build()
        }
        builder.client(this.httpClient)
        return builder.build()
    }
}
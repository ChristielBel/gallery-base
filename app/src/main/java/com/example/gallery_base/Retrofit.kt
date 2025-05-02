package com.example.gallery_base

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val CONNECT_TIMEOUT_INTERVAL_SEC = 10L
private const val READ_TIMEOUT_INTERVAL_SEC = 10L
private const val WRITE_TIMEOUT_INTERVAL_SEC = 10L
private const val BASE_URL = "https://your-api-url.com/api/"


object Retrofit {
    var gson = GsonBuilder()
        .setDateFormat("yyyy.MM.dd")
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val galleryApiService: GalleryApiService by lazy {
        retrofit.create(GalleryApiService::class.java)
    }
}
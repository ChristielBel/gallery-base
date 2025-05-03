package com.example.gallery_base.API

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:8080/" //https://localhost:8080

object Retrofit {
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .create()


    private val okHttpClient = OkHttpClient.Builder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val artistController: ArtistController by lazy {
        retrofit.create(ArtistController::class.java)
    }
    val exhibitionController: ExhibitionController by lazy {
        retrofit.create(ExhibitionController::class.java)
    }
    val paintingController: PaintingController by lazy {
        retrofit.create(PaintingController::class.java)
    }
}
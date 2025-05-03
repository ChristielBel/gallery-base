package com.example.gallery_base.API

import com.example.gallery_base.dto.ArtistDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface ArtistController {
    @GET("artists")
    suspend fun getAllArtists(): Response<List<ArtistDTO>>

    @POST("artists")
    suspend fun createArtist(@Body artist: ArtistDTO): Response<Unit>

    @PUT("artists/{id}")
    suspend fun updateArtist(
        @Path("id") id: UUID,
        @Body artist: ArtistDTO
    ): Response<Unit>

    @DELETE("artists/{id}")
    suspend fun deleteArtist(@Path("id") id: UUID): Response<Unit>
}
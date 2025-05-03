package com.example.gallery_base.API

import com.example.gallery_base.dto.ExhibitionDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface ExhibitionController {
    @GET("exhibitions")
    suspend fun getAllExhibitions(): Response<List<ExhibitionDTO>>

    @POST("exhibitions")
    suspend fun createExhibition(@Body exhibition: ExhibitionDTO): Response<Unit>

    @PUT("exhibitions/{id}")
    suspend fun updateExhibition(
        @Path("id") id: UUID,
        @Body exhibition: ExhibitionDTO
    ): Response<Unit>

    @DELETE("exhibitions/{id}")
    suspend fun deleteExhibition(@Path("id") id: UUID): Response<Unit>
}
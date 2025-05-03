package com.example.gallery_base.API

import com.example.gallery_base.dto.PaintingDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface PaintingController {
    @GET("paintings")
    suspend fun getAllPaintings(): Response<List<PaintingDTO>>

    @POST("paintings")
    suspend fun createPainting(@Body painting: PaintingDTO): Response<Unit>

    @PUT("paintings/{id}")
    suspend fun updatePainting(
        @Path("id") id: UUID,
        @Body painting: PaintingDTO
    ): Response<Unit>

    @DELETE("paintings/{id}")
    suspend fun deletePainting(@Path("id") id: UUID): Response<Unit>
}
package com.example.gallery_base

import com.example.gallery_base.dto.ArtistDTO
import com.example.gallery_base.dto.ExhibitionDTO
import com.example.gallery_base.dto.PaintingDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

// ApiService.kt
interface GalleryApiService {
    @GET("exhibitions")
    suspend fun getExhibitions(): List<ExhibitionDTO>

    @GET("exhibitions/{id}/artists")
    suspend fun getArtistsByExhibition(@Path("id") exhibitionId: UUID): List<ArtistDTO>

    @GET("artists/{id}/paintings")
    suspend fun getPaintingsByArtist(@Path("id") artistId: UUID): List<PaintingDTO>

    @POST("paintings")
    suspend fun createPainting(@Body painting: PaintingDTO): PaintingDTO

    @PUT("paintings/{id}")
    suspend fun updatePainting(@Path("id") id: UUID, @Body painting: PaintingDTO): PaintingDTO

    @DELETE("paintings/{id}")
    suspend fun deletePainting(@Path("id") id: UUID)
}
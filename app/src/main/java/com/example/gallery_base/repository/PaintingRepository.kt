package com.example.gallery_base.repository

import com.example.gallery_base.data.Painting
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface PaintingRepository {
    fun getAllPaintings(): Flow<List<Painting>>

    fun getByArtist(artistId: UUID): Flow<List<Painting>>

    suspend fun insertPainting(painting: Painting)

    suspend fun insertAllPaintings(list: List<Painting>)

    suspend fun updatePainting(painting: Painting)

    suspend fun deletePainting(painting: Painting)

    suspend fun deleteAllPaintings()
}
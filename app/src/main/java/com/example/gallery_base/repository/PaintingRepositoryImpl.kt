package com.example.gallery_base.repository

import android.util.Log
import com.example.gallery_base.API.PaintingController
import com.example.gallery_base.data.Painting
import com.example.gallery_base.dto.toDTO
import com.example.gallery_base.dto.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class PaintingRepositoryImpl(private val api: PaintingController) : PaintingRepository {
    override fun getAllPaintings(): Flow<List<Painting>> = flow {
        try {
            val response = api.getAllPaintings()
            if (response.isSuccessful) {
                val paintings = response.body()?.map { it.toEntity() } ?: emptyList()
                emit(paintings)
                Log.i("Painting", "AAAAAA")
            } else {
                throw Exception("Failed to fetch paintings: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }

    override fun getByArtist(artistId: UUID): Flow<List<Painting>> {
        return getAllPaintings().map { paintings ->
            paintings.filter { it.artistId == artistId }
        }
    }

    override suspend fun insertPainting(painting: Painting) {
        val response = api.createPainting(painting.toDTO())
        if (!response.isSuccessful) {
            throw Exception("Failed to create painting: ${response.code()}")
        }
    }

    override suspend fun insertAllPaintings(list: List<Painting>) {
        list.forEach { painting ->
            insertPainting(painting)
        }
    }

    override suspend fun updatePainting(painting: Painting) {
        val response = api.updatePainting(painting.id, painting.toDTO())
        if (!response.isSuccessful) {
            throw Exception("Failed to update painting: ${response.code()}")
        }
    }

    override suspend fun deletePainting(painting: Painting) {
        val response = api.deletePainting(painting.id)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete painting: ${response.code()}")
        }
    }

    override suspend fun deleteAllPaintings() {
        throw UnsupportedOperationException("Bulk delete not supported by API")
    }
}
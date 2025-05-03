package com.example.gallery_base.repository

import android.util.Log
import com.example.gallery_base.API.ExhibitionController
import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.dto.toDTO
import com.example.gallery_base.dto.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.log

class ExhibitionRepositoryImpl(private val api: ExhibitionController) : ExhibitionRepository {
    override fun getAllExhibitions(): Flow<List<Exhibition>> = flow {
        try {
            val response = api.getAllExhibitions()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("API Response", "Response Body: $body")
                    val artists = body.map { it.toEntity() }
                }
                val exhibitions = response.body()?.map { it.toEntity() } ?: emptyList()
                emit(exhibitions)
                Log.i("Exhibition", "AAAAAA")
            } else {
                throw Exception("Failed to fetch exhibitions: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }

    override suspend fun insertExhibition(exhibition: Exhibition) {
        val response = api.createExhibition(exhibition.toDTO())
        Log.i("Exhibition", "BBBBBBB")
        if (!response.isSuccessful) {
            throw Exception("Failed to create exhibition: ${response.code()}")
        }
    }

    override suspend fun insertAllExhibitions(list: List<Exhibition>) {
        list.forEach { exhibition ->
            insertExhibition(exhibition)
        }
    }

    override suspend fun updateExhibition(exhibition: Exhibition) {
        val response = api.updateExhibition(exhibition.id, exhibition.toDTO())
        if (!response.isSuccessful) {
            throw Exception("Failed to update exhibition: ${response.code()}")
        }
    }

    override suspend fun deleteExhibition(exhibition: Exhibition) {
        val response = api.deleteExhibition(exhibition.id)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete exhibition: ${response.code()}")
        }
    }

    override suspend fun deleteAllExhibitions() {
        throw UnsupportedOperationException("Bulk delete not supported by API")
    }
}
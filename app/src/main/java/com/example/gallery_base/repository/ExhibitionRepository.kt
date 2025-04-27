package com.example.gallery_base.repository

import com.example.gallery_base.data.Exhibition
import kotlinx.coroutines.flow.Flow

interface ExhibitionRepository {
    fun getAllExhibitions(): Flow<List<Exhibition>>

    suspend fun insertExhibition(exhibition: Exhibition)

    suspend fun insertAllExhibitions(list: List<Exhibition>)

    suspend fun updateExhibition(exhibition: Exhibition)

    suspend fun deleteExhibition(exhibition: Exhibition)

    suspend fun deleteAllExhibitions()
}
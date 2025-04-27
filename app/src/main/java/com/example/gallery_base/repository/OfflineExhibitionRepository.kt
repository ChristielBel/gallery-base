package com.example.gallery_base.repository

import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.database.ExhibitionDAO
import kotlinx.coroutines.flow.Flow

class OfflineExhibitionRepository(private val exhibitionDAO: ExhibitionDAO) : ExhibitionRepository {
    override fun getAllExhibitions(): Flow<List<Exhibition>> = exhibitionDAO.getAllExhibitions()

    override suspend fun insertExhibition(exhibition: Exhibition) =
        exhibitionDAO.insertExhibition(exhibition)

    override suspend fun insertAllExhibitions(list: List<Exhibition>) =
        exhibitionDAO.insertAllExhibitions(list)

    override suspend fun updateExhibition(exhibition: Exhibition) =
        exhibitionDAO.updateExhibition(exhibition)

    override suspend fun deleteExhibition(exhibition: Exhibition) =
        exhibitionDAO.deleteExhibition(exhibition)

    override suspend fun deleteAllExhibitions() = exhibitionDAO.deleteAllExhibitions()
}
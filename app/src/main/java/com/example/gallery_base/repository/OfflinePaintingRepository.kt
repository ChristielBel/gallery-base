package com.example.gallery_base.repository

import com.example.gallery_base.data.Painting
import com.example.gallery_base.database.PaintingDAO
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class OfflinePaintingRepository(private val paintingDAO: PaintingDAO) : PaintingRepository {
    override fun getAllPaintings(): Flow<List<Painting>> = paintingDAO.getAllPaintings()

    override fun getByArtist(artistId: UUID): Flow<List<Painting>> =
        paintingDAO.getByArtist(artistId)

    override suspend fun insertPainting(painting: Painting) = paintingDAO.insertPainting(painting)

    override suspend fun insertAllPaintings(list: List<Painting>) =
        paintingDAO.insertAllPaintings(list)

    override suspend fun updatePainting(painting: Painting) = paintingDAO.updatePainting(painting)

    override suspend fun deletePainting(painting: Painting) = paintingDAO.deletePainting(painting)

    override suspend fun deleteAllPaintings() = paintingDAO.deleteAllPaintings()
}
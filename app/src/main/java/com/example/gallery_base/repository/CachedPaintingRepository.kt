package com.example.gallery_base.repository

import com.example.gallery_base.data.Painting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class CachedPaintingRepository(
    private val networkRepo: PaintingRepositoryImpl,
    private val offlineRepo: OfflinePaintingRepository
) : PaintingRepository {
    override fun getAllPaintings(): Flow<List<Painting>> = flow {
        try {
            val networkPaintings = networkRepo.getAllPaintings().first()
            offlineRepo.deleteAllPaintings()
            offlineRepo.insertAllPaintings(networkPaintings)
            emit(networkPaintings)
        } catch (e: Exception) {
            emit(offlineRepo.getAllPaintings().first())
        }
    }

    override fun getByArtist(artistId: UUID): Flow<List<Painting>> {
        return getAllPaintings().map { paintings ->
            paintings.filter { it.artistId == artistId }
        }
    }

    override suspend fun insertPainting(painting: Painting) {
        try {
            networkRepo.insertPainting(painting)
            offlineRepo.insertPainting(painting)
        } catch (e: Exception) {
            offlineRepo.insertPainting(painting)
            throw Exception("Saved locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun insertAllPaintings(list: List<Painting>) {
        try {
            networkRepo.insertAllPaintings(list)
            offlineRepo.insertAllPaintings(list)
        } catch (e: Exception) {
            offlineRepo.insertAllPaintings(list)
            throw Exception("Saved locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun updatePainting(painting: Painting) {
        try {
            networkRepo.updatePainting(painting)
            offlineRepo.updatePainting(painting)
        } catch (e: Exception) {
            offlineRepo.updatePainting(painting)
            throw Exception("Updated locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun deletePainting(painting: Painting) {
        try {
            networkRepo.deletePainting(painting)
            offlineRepo.deletePainting(painting)
        } catch (e: Exception) {
            offlineRepo.deletePainting(painting)
            throw Exception("Deleted locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun deleteAllPaintings() {
        try {
            networkRepo.deleteAllPaintings()
            offlineRepo.deleteAllPaintings()
        } catch (e: Exception) {
            offlineRepo.deleteAllPaintings()
            throw Exception("Cleared locally. Will sync when online: ${e.message}")
        }
    }
}
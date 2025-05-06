package com.example.gallery_base.repository

import android.util.Log
import com.example.gallery_base.data.Artist
import com.example.gallery_base.data.Exhibition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class CachedArtistRepository(
    private val networkRepo: ArtistRepositoryImpl,
    private val offlineRepo: OfflineArtistRepository
) : ArtistRepository {
    override fun getAllArtists(): Flow<List<Artist>> = flow {
        try {
            Log.e("Cached", "All")
            val networkArtists = networkRepo.getAllArtists().first()
            offlineRepo.deleteAllArtists()
            offlineRepo.insertAllArtists(networkArtists)
            emit(networkArtists)
        } catch (e: Exception) {
            emit(offlineRepo.getAllArtists().first())
        }
    }

    override fun getByExhibition(exhibitionId: UUID): Flow<List<Artist>> {
        Log.d("CachedArtistRepo", "getByExhibition for $exhibitionId")
        return getAllArtists().map { artists ->
            val filtered = artists.filter { it.exhibitionId == exhibitionId }
            Log.d(
                "CachedArtistRepo",
                "Filtered ${filtered.size} artists for exhibition $exhibitionId"
            )
            filtered
        }
    }

    override suspend fun insertArtist(artist: Artist) {
        try {
            networkRepo.insertArtist(artist)
            offlineRepo.insertArtist(artist)
        } catch (e: Exception) {
            offlineRepo.insertArtist(artist)
            throw Exception("Saved locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun insertAllArtists(list: List<Artist>) {
        try {
            networkRepo.insertAllArtists(list)
            offlineRepo.insertAllArtists(list)
        } catch (e: Exception) {
            offlineRepo.insertAllArtists(list)
            throw Exception("Saved locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun updateArtist(artist: Artist) {
        try {
            networkRepo.updateArtist(artist)
            offlineRepo.updateArtist(artist)
        } catch (e: Exception) {
            offlineRepo.updateArtist(artist)
            throw Exception("Updated locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun deleteArtist(artist: Artist) {
        try {
            networkRepo.deleteArtist(artist)
            offlineRepo.deleteArtist(artist)
        } catch (e: Exception) {
            offlineRepo.deleteArtist(artist)
            throw Exception("Deleted locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun deleteAllArtists() {
        try {
            networkRepo.deleteAllArtists()
            offlineRepo.deleteAllArtists()
        } catch (e: Exception) {
            offlineRepo.deleteAllArtists()
            throw Exception("Cleared locally. Will sync when online: ${e.message}")
        }
    }
}
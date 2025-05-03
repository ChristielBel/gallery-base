package com.example.gallery_base.repository

import android.util.Log
import com.example.gallery_base.API.ArtistController
import com.example.gallery_base.data.Artist
import com.example.gallery_base.dto.toDTO
import com.example.gallery_base.dto.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ArtistRepositoryImpl(private val api: ArtistController) : ArtistRepository  {
    override fun getAllArtists(): Flow<List<Artist>> = flow {
        try {
            val response = api.getAllArtists()
            if (response.isSuccessful) {
                val artists = response.body()?.map { it.toEntity() } ?: emptyList()
                emit(artists)
                Log.i("Artist", "AAAAAA")
            } else {
                throw Exception("Failed to fetch artists: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }

    override fun getByExhibition(exhibitionId: UUID): Flow<List<Artist>> {
        return getAllArtists().map { artists ->
            artists.filter { it.exhibitionId == exhibitionId }
        }
    }

    override suspend fun insertArtist(artist: Artist) {
        val response = api.createArtist(artist.toDTO())
        if (!response.isSuccessful) {
            throw Exception("Failed to create artist: ${response.code()}")
        }
    }

    override suspend fun insertAllArtists(list: List<Artist>) {
        list.forEach { artist ->
            insertArtist(artist)
        }
    }

    override suspend fun updateArtist(artist: Artist) {
        val response = api.updateArtist(artist.id, artist.toDTO())
        if (!response.isSuccessful) {
            throw Exception("Failed to update artist: ${response.code()}")
        }
    }

    override suspend fun deleteArtist(artist: Artist) {
        val response = api.deleteArtist(artist.id)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete artist: ${response.code()}")
        }
    }

    override suspend fun deleteAllArtists() {
        throw UnsupportedOperationException("Bulk delete not supported by API")
    }
}
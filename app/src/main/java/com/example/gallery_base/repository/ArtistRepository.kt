package com.example.gallery_base.repository

import com.example.gallery_base.data.Artist
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ArtistRepository {
    fun getAllArtists(): Flow<List<Artist>>

    fun getByExhibition(exhibitionId: UUID): Flow<List<Artist>>

    suspend fun insertArtist(artist: Artist)

    suspend fun insertAllArtists(list: List<Artist>)

    suspend fun updateArtist(artist: Artist)

    suspend fun deleteArtist(artist: Artist)

    suspend fun deleteAllArtists()
}
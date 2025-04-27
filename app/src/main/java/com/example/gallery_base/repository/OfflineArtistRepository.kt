package com.example.gallery_base.repository

import com.example.gallery_base.data.Artist
import com.example.gallery_base.database.ArtistDAO
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class OfflineArtistRepository(private val artistDAO: ArtistDAO) : ArtistRepository {
    override fun getAllArtists(): Flow<List<Artist>> = artistDAO.getAllArtists()

    override fun getByExhibition(exhibitionId: UUID): Flow<List<Artist>> =
        artistDAO.getByExhibition(exhibitionId)

    override suspend fun insertArtist(artist: Artist) = artistDAO.insertArtist(artist)

    override suspend fun insertAllArtists(list: List<Artist>) = artistDAO.insertAllArtists(list)

    override suspend fun updateArtist(artist: Artist) = artistDAO.updateArtist(artist)

    override suspend fun deleteArtist(artist: Artist) = artistDAO.deleteArtist(artist)

    override suspend fun deleteAllArtists() = artistDAO.deleteAllArtists()
}
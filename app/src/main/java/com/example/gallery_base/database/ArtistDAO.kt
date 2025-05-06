package com.example.gallery_base.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gallery_base.data.Artist
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ArtistDAO {
    @Query("SELECT * FROM artist ORDER BY name")
    fun getAllArtists(): Flow<List<Artist>>

    @Query("SELECT * FROM artist WHERE exhibition_id = :exhibitionId")
    fun getByExhibition(exhibitionId: UUID): Flow<List<Artist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: Artist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArtists(list: List<Artist>)

    @Update
    suspend fun updateArtist(artist: Artist)

    @Delete
    suspend fun deleteArtist(artist: Artist)

    @Query("DELETE FROM artist")
    suspend fun deleteAllArtists()
}
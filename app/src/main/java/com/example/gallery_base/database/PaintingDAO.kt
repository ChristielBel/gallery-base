package com.example.gallery_base.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gallery_base.data.Painting
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface PaintingDAO {
    @Query("SELECT * FROM painting ORDER BY title")
    fun getAllPaintings(): Flow<List<Painting>>

    @Query("SELECT * FROM painting WHERE artist_id=:artistId")
    fun getByArtist(artistId: UUID): Flow<List<Painting>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPainting(painting: Painting)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPaintings(list : List<Painting>)

    @Update
    suspend fun updatePainting(painting: Painting)

    @Delete
    suspend fun deletePainting(painting: Painting)

    @Query("DELETE FROM painting")
    suspend fun deleteAllPaintings()
}
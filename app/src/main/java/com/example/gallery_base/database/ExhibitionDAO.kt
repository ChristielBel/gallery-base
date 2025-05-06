package com.example.gallery_base.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gallery_base.data.Exhibition
import kotlinx.coroutines.flow.Flow

@Dao
interface ExhibitionDAO {
    @Query("SELECT * FROM exhibition ORDER BY title")
    fun getAllExhibitions(): Flow<List<Exhibition>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExhibition(exhibition: Exhibition)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExhibitions(list: List<Exhibition>)

    @Update
    suspend fun updateExhibition(exhibition: Exhibition)

    @Delete
    suspend fun deleteExhibition(exhibition: Exhibition)

    @Query("DELETE FROM exhibition")
    suspend fun deleteAllExhibitions()
}
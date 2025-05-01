package com.example.gallery_base.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(
    tableName = "painting",
    indices = [Index("artist_id")],
    foreignKeys = [
        ForeignKey(
            entity = Artist::class,
            parentColumns = ["id"],
            childColumns = ["artist_id"]
        )
    ]
)
data class Painting(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("artist_id")
    val artistId: UUID?,
    @ColumnInfo("date_of_writing")
    val dateOfWriting: Date = Date(),
    @ColumnInfo("description")
    val description: String?
)

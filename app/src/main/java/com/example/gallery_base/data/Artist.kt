package com.example.gallery_base.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "artist",
    foreignKeys = [
        ForeignKey(
            entity = Exhibition::class,
            parentColumns = ["id"],
            childColumns = ["exhibition_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Artist(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("exhibition_id")
    val exhibitionId: UUID?
)
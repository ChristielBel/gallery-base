package com.example.gallery_base.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gallery_base.dto.ExhibitionDTO
import java.util.UUID

@Entity(
    tableName = "exhibition"
)
data class Exhibition(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo("title")
    val title: String
)

package com.example.gallery_base.dto

import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.data.Painting
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class PaintingDTO(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    @SerializedName("title")
    val title: String,
    @SerializedName("artistId")
    val artistId: UUID?,
    @SerializedName("dateOfWriting")
    val dateOfWriting: Date = Date(),
    @SerializedName("description")
    val description: String?
)

fun PaintingDTO.toEntity() = Painting(id, title, artistId, dateOfWriting, description)
fun Painting.toDTO() = PaintingDTO(id, title, artistId, dateOfWriting, description)
package com.example.gallery_base.dto

import com.example.gallery_base.data.Artist
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ArtistDTO(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    @SerializedName("name")
    val name: String,
    @SerializedName("exhibition_id")
    val exhibitionId: UUID?
)

fun ArtistDTO.toEntity() = Artist(id, name, exhibitionId)
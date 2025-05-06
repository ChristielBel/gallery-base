package com.example.gallery_base.dto

import com.example.gallery_base.data.Artist
import com.example.gallery_base.data.Exhibition
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ArtistDTO(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    @SerializedName("name")
    val name: String,
    @SerializedName("exhibitionId")
    val exhibitionId: UUID?
)

fun ArtistDTO.toEntity() = Artist(id, name, exhibitionId)
fun Artist.toDTO(): ArtistDTO {
    return ArtistDTO(
        id = this.id,
        name = this.name,
        exhibitionId = this.exhibitionId
    )
}

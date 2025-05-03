package com.example.gallery_base.dto

import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.data.Painting
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ExhibitionDTO(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    @SerializedName("title")
    val title: String
)

fun ExhibitionDTO.toEntity() = Exhibition(id, title)
fun Exhibition.toDTO() = ExhibitionDTO(id, title)

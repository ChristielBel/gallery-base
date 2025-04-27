package com.example.gallery_base.data

import android.content.Context
import com.example.gallery_base.database.GalleryDatabase
import com.example.gallery_base.repository.ArtistRepository
import com.example.gallery_base.repository.ExhibitionRepository
import com.example.gallery_base.repository.OfflineArtistRepository
import com.example.gallery_base.repository.OfflineExhibitionRepository
import com.example.gallery_base.repository.OfflinePaintingRepository
import com.example.gallery_base.repository.PaintingRepository

interface IocContainer {
    val artistRepository: ArtistRepository
    val exhibitionRepository: ExhibitionRepository
    val paintingRepository: PaintingRepository
}

class AppContainer(private val context: Context) : IocContainer {
    override val exhibitionRepository: OfflineExhibitionRepository by lazy {
        OfflineExhibitionRepository(GalleryDatabase.getDatabase(context).getExhibitionDAO())
    }

    override val artistRepository: OfflineArtistRepository by lazy {
        OfflineArtistRepository(GalleryDatabase.getDatabase(context).getArtistDAO())
    }

    override val paintingRepository: OfflinePaintingRepository by lazy {
        OfflinePaintingRepository(GalleryDatabase.getDatabase(context).getPaintingDAO())
    }
}
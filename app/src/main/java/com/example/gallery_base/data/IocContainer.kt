package com.example.gallery_base.data

import android.content.Context
import com.example.gallery_base.API.Retrofit
import com.example.gallery_base.database.GalleryDatabase
import com.example.gallery_base.repository.ArtistRepository
import com.example.gallery_base.repository.ArtistRepositoryImpl
import com.example.gallery_base.repository.CachedArtistRepository
import com.example.gallery_base.repository.CachedExhibitionRepository
import com.example.gallery_base.repository.CachedPaintingRepository
import com.example.gallery_base.repository.ExhibitionRepository
import com.example.gallery_base.repository.ExhibitionRepositoryImpl
import com.example.gallery_base.repository.OfflineArtistRepository
import com.example.gallery_base.repository.OfflineExhibitionRepository
import com.example.gallery_base.repository.OfflinePaintingRepository
import com.example.gallery_base.repository.PaintingRepository
import com.example.gallery_base.repository.PaintingRepositoryImpl

interface IocContainer {
    val artistRepository: ArtistRepository
    val exhibitionRepository: ExhibitionRepository
    val paintingRepository: PaintingRepository
}

class AppContainer(private val context: Context) : IocContainer {
  private val database by lazy { GalleryDatabase.getDatabase(context) }

    private val exhibitionRepositoryImpl by lazy {
        ExhibitionRepositoryImpl(Retrofit.exhibitionController)
    }

    private val artistRepositoryImpl by lazy {
        ArtistRepositoryImpl(Retrofit.artistController)
    }

    private val paintingRepositoryImpl by lazy {
        PaintingRepositoryImpl(Retrofit.paintingController)
    }

    private val offlineExhibitionRepo by lazy {
        OfflineExhibitionRepository(database.getExhibitionDAO())
    }
    private val offlineArtistRepo by lazy {
        OfflineArtistRepository(database.getArtistDAO())
    }
    private val offlinePaintingRepo by lazy {
        OfflinePaintingRepository(database.getPaintingDAO())
    }

    override val exhibitionRepository: ExhibitionRepository by lazy {
        CachedExhibitionRepository(exhibitionRepositoryImpl, offlineExhibitionRepo)
    }

    override val artistRepository: ArtistRepository by lazy {
        CachedArtistRepository(artistRepositoryImpl, offlineArtistRepo)
    }

    override val paintingRepository: PaintingRepository by lazy {
        CachedPaintingRepository(paintingRepositoryImpl, offlinePaintingRepo)
    }
}
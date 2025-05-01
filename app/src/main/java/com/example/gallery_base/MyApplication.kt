package com.example.gallery_base

import android.app.Application
import android.content.Context
import com.example.gallery_base.repository.ExhibitionRepository
import com.example.gallery_base.repository.ArtistRepository
import com.example.gallery_base.repository.PaintingRepository
import com.example.gallery_base.database.GalleryDatabase
import com.example.gallery_base.repository.OfflineArtistRepository
import com.example.gallery_base.repository.OfflineExhibitionRepository
import com.example.gallery_base.repository.OfflinePaintingRepository

class MyApplication : Application() {
    lateinit var exhibitionRepository: ExhibitionRepository
    lateinit var artistRepository: ArtistRepository
    lateinit var paintingRepository: PaintingRepository

    override fun onCreate() {
        super.onCreate()

        // Инициализация репозиториев с использованием GalleryDatabase
        val database = GalleryDatabase.getDatabase(applicationContext)

        exhibitionRepository = OfflineExhibitionRepository(database.getExhibitionDAO())
        artistRepository = OfflineArtistRepository(database.getArtistDAO())
        paintingRepository = OfflinePaintingRepository(database.getPaintingDAO())
    }

    companion object {
        private var instance: MyApplication? = null

        // Получение контекста приложения
        val context: Context
            get() = instance!!.applicationContext

        // Инициализация instance при создании объекта
        fun getInstance(): MyApplication {
            return instance ?: throw IllegalStateException("Application not initialized")
        }
    }

    init {
        instance = this
    }
}

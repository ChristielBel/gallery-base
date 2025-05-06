package com.example.gallery_base

import android.app.Application
import android.content.Context
import com.example.gallery_base.data.AppContainer
import com.example.gallery_base.repository.ExhibitionRepository
import com.example.gallery_base.repository.ArtistRepository
import com.example.gallery_base.repository.PaintingRepository
import com.example.gallery_base.database.GalleryDatabase
import com.example.gallery_base.repository.OfflineArtistRepository
import com.example.gallery_base.repository.OfflineExhibitionRepository
import com.example.gallery_base.repository.OfflinePaintingRepository

class MyApplication : Application() {
    val appContainer by lazy { AppContainer(this) }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        private var instance: MyApplication? = null

        val context: Context
            get() = instance!!.applicationContext

        fun getInstance(): MyApplication {
            return instance ?: throw IllegalStateException("Application not initialized")
        }
    }

    init {
        instance = this
    }
}

package com.example.gallery_base.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gallery_base.data.Artist
import com.example.gallery_base.data.Exhibition
import com.example.gallery_base.data.Painting

@Database(
    version = 1,
    entities = [
        Exhibition::class,
        Artist::class,
        Painting::class
    ]
)
abstract class GalleryDatabase : RoomDatabase() {
    abstract fun getExhibitionDAO(): ExhibitionDAO

    abstract fun getArtistDAO(): ArtistDAO

    abstract fun getPaintingDAO(): PaintingDAO

    companion object {
        @Volatile
        private var Instance: GalleryDatabase? = null

        fun getDatabase(context: Context): GalleryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GalleryDatabase::class.java, "gallery_base")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
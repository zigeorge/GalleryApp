package com.geo.galleryapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geo.galleryapp.models.Data

@Database(
    entities = [Data::class],
    version = 1,
    exportSchema = false
)
abstract class GalleryDb: RoomDatabase() {

    abstract fun galleryDao(): GalleryDao

}
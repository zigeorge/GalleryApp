package com.geo.galleryapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.geo.galleryapp.models.ImageData
import com.geo.galleryapp.models.RemoteKeys

@Database(
    entities = [ImageData::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GalleryDb: RoomDatabase() {

    abstract fun imageDataDao(): ImageDataDao

    abstract fun remoteKeysDao(): RemoteKeysDao

}
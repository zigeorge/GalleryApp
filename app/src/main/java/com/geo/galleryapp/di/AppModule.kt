package com.geo.galleryapp.di

import android.content.Context
import androidx.room.Room
import com.geo.galleryapp.api.GalleryApi
import com.geo.galleryapp.api.RetrofitInstance
import com.geo.galleryapp.db.GalleryDb
import com.geo.galleryapp.other.Constants.DB_NAME
import com.geo.galleryapp.repository.GalleryDbRepository
import com.geo.galleryapp.repository.GalleryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApiClient(): GalleryApi {
        return RetrofitInstance.api
    }

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, GalleryDb::class.java, DB_NAME).build()

    @Provides
    fun provideDao(
        db: GalleryDb
    ) = db.imageDataDao()

    @Provides
    fun provideRepository(
        api: GalleryApi,
        db: GalleryDb
    ) = GalleryDbRepository(api, db) as GalleryRepository
}
package com.geo.galleryapp.di

import android.content.Context
import androidx.room.Room
import com.geo.galleryapp.api.GalleryAPI
import com.geo.galleryapp.api.RetrofitInstance
import com.geo.galleryapp.db.GalleryDb
import com.geo.galleryapp.other.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiClient(): GalleryAPI {
        return RetrofitInstance.api
    }

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, GalleryDb::class.java, DB_NAME).build()

    @Singleton
    @Provides
    fun provideDao(
        db: GalleryDb
    ) = db.galleryDao()
}
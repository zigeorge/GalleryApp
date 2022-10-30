package com.geo.galleryapp.di

import android.content.Context
import androidx.room.Room
import com.geo.galleryapp.db.GalleryDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    @Provides
    @Named("test_gallery_db")
    fun provideInMemoryDatabase(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(context, GalleryDb::class.java)
        .allowMainThreadQueries()
        .build()
}
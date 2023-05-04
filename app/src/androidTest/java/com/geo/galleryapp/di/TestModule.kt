package com.geo.galleryapp.di

import android.content.Context
import androidx.room.Room
import com.geo.galleryapp.api.FakeGalleryApiAndroidTest
import com.geo.galleryapp.api.GalleryApi
import com.geo.galleryapp.db.GalleryDb
import com.geo.galleryapp.other.Constants
import com.geo.galleryapp.repository.GalleryDbRepository
import com.geo.galleryapp.repository.GalleryRepository
import com.geo.galleryapp.repository.ImageDataFactoryAndroidTest
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestModule {

    @Provides
    @Named("test_gallery_db")
    fun provideInMemoryDatabase(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(context, GalleryDb::class.java)
        .allowMainThreadQueries()
        .build()

    @Provides
    fun provideApiClient(): GalleryApi {
        val fakeApi = FakeGalleryApiAndroidTest()
        fakeApi.addImageDataList(ImageDataFactoryAndroidTest().fakeImages(60))
        return fakeApi
    }

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, GalleryDb::class.java, Constants.DB_NAME).build()

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
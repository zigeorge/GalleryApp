package com.geo.galleryapp.db

import androidx.paging.PagingSource
import androidx.test.filters.SmallTest
import com.geo.galleryapp.di.AppModule
import com.geo.galleryapp.repository.ImageDataFactoryAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

//@UninstallModules(AppModule::class)
@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ImageDataDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_gallery_db")
    lateinit var database: GalleryDb
    private lateinit var dao: ImageDataDao

    private lateinit var imageDataFactory: ImageDataFactoryAndroidTest


    @Before
    fun setUp() {
        hiltRule.inject()
        dao = database.imageDataDao()
        imageDataFactory = ImageDataFactoryAndroidTest()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun verifyInsertAll() = runTest {
        val list = imageDataFactory.fakeImages()
        dao.insertAll(list)

        val count = dao.count()
        assertThat(count).isEqualTo(list.size)
    }

    @Test
    fun galleryImagesPagingSource() = runBlocking {
        val list = imageDataFactory.fakeImages()
        dao.insertAll(list)

        val actual = dao.galleryImages("").load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )
        val expected = PagingSource.LoadResult.Page(
            data = listOf(list[0], list[1]),
            prevKey = null,
            nextKey = 2,
            itemsBefore = 0,
            itemsAfter = 98
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun galleryImagesPagingSourceIsNotNullWhenNoDataInTable() = runTest {
        val data = dao.galleryImages("").load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )
        assertThat(data).isNotNull()
    }

    @Test
    fun deleteRemovesAllDataFromTable() = runTest {
        dao.insertAll(imageDataFactory.fakeImages())

        dao.delete()

        assertThat(dao.count()).isEqualTo(0)
    }


}


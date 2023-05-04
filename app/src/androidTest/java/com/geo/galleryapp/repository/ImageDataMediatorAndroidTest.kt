package com.geo.galleryapp.repository

import androidx.paging.*
import androidx.test.filters.SmallTest
import com.geo.galleryapp.api.FakeGalleryApiAndroidTest
import com.geo.galleryapp.db.GalleryDb
import com.geo.galleryapp.models.ImageData
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ImageDataMediatorAndroidTest {
    private val fakeApi = FakeGalleryApiAndroidTest()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_gallery_db")
    lateinit var fakeDb: GalleryDb

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        fakeDb.close()
        fakeApi.reset()
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        fakeApi.addImageDataList(ImageDataFactoryAndroidTest().fakeImages())
        val remoteMediator = ImageDataMediator(
            fakeDb,
            fakeApi,
            ""
        )
        val pagingState = PagingState<Int, ImageData>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runTest {
        val remoteMediator = ImageDataMediator(
            fakeDb,
            fakeApi,
            ""
        )
        val pagingState = PagingState<Int, ImageData>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runTest {
        fakeApi.addImageDataList(ImageDataFactoryAndroidTest().fakeImages())
        FakeGalleryApiAndroidTest.error = true
        val remoteMediator = ImageDataMediator(
            fakeDb,
            fakeApi,
            ""
        )
        val pagingState = PagingState<Int, ImageData>(
            listOf(),
            10,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is RemoteMediator.MediatorResult.Error).isTrue()
    }

    @Test
    fun appendLoadSuccessAndEndOfPaginationWhenNoMoreData() = runTest {
        fakeApi.addImageDataList(ImageDataFactoryAndroidTest().fakeImages())
        val remoteMediator = ImageDataMediator(
            fakeDb,
            fakeApi,
            ""
        )
        val pagingState = PagingState<Int, ImageData>(
            listOf(),
            10,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.APPEND, pagingState)
        assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

}
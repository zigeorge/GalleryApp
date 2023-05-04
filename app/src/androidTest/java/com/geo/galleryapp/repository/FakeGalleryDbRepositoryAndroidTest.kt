package com.geo.galleryapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.geo.galleryapp.api.GalleryApi
import com.geo.galleryapp.db.GalleryDb
import com.geo.galleryapp.models.ImageData
import com.geo.galleryapp.other.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FakeGalleryDbRepositoryAndroidTest @Inject constructor(
    private val api: GalleryApi,
    private val db: GalleryDb
) : GalleryRepository {


    override fun getImage(query: String): Flow<PagingData<ImageData>> {
        val pagingSourceFactory = { db.imageDataDao().galleryImages("%$query%") }
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = Constants.PAGE_SIZE
            ),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = ImageDataMediator(db, api, query)
        ).flow
    }
}
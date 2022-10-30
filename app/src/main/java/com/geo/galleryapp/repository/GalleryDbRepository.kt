package com.geo.galleryapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.geo.galleryapp.api.GalleryAPI
import com.geo.galleryapp.db.GalleryDb
import com.geo.galleryapp.models.ImageData
import com.geo.galleryapp.other.Constants.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GalleryDbRepository @Inject constructor(
    private val api: GalleryAPI,
    private val db: GalleryDb
): GalleryRepository {

    override fun getImage(query: String): Flow<PagingData<ImageData>> {
        val pagingSourceFactory = { db.imageDataDao().galleryImages("%$query%") }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = ImageDataMediator(db, api, query)
        ).flow
    }
}
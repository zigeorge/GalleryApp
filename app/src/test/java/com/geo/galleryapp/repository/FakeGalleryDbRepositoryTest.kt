package com.geo.galleryapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.geo.galleryapp.api.GalleryApi
import javax.inject.Inject

class FakeGalleryDbRepositoryTest @Inject constructor(
    private val api: GalleryApi
) : GalleryRepository {

    override fun getImage(query: String)= Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        )
    ) {
        ImageDataPagingSourceTest(
            api = api
        )
    }.flow
}
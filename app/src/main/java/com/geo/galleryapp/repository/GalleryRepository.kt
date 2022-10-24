package com.geo.galleryapp.repository

import androidx.paging.PagingData
import com.geo.galleryapp.models.ImageData
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getImage(
        query: String
    ): Flow<PagingData<ImageData>>

    fun getHudaiImage(): Flow<List<ImageData>>
}
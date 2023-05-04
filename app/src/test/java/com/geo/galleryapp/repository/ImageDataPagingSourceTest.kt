package com.geo.galleryapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.geo.galleryapp.api.GalleryApi
import com.geo.galleryapp.models.ImageData
import retrofit2.HttpException
import java.io.IOException

class ImageDataPagingSourceTest(
    private val api: GalleryApi
): PagingSource<Int, ImageData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageData> {
        return try {
            CURRENT_PAGE++
            val items = api.searchImages(
                10, CURRENT_PAGE, ""
            )?.data ?: ArrayList()

            LoadResult.Page(
                data = items,
                prevKey = CURRENT_PAGE - 1,
                nextKey = CURRENT_PAGE + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ImageData>): Int {
        /**
         * The name field is a unique identifier for post items.
         * (no it is not the title of the post :) )
         * https://www.reddit.com/dev/api
         */
        CURRENT_PAGE++
        return REFRESH
    }

    companion object {
        const val REFRESH = 0

        var CURRENT_PAGE = 0
    }

}
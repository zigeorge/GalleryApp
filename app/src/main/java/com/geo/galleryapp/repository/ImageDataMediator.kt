package com.geo.galleryapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.geo.galleryapp.api.GalleryApi
import com.geo.galleryapp.db.GalleryDb
import com.geo.galleryapp.db.RemoteKeysDao
import com.geo.galleryapp.models.ImageData
import com.geo.galleryapp.models.RemoteKeys
import com.geo.galleryapp.other.Constants.DEFAULT_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ImageDataMediator(
    private val db: GalleryDb,
    private val api: GalleryApi,
    private val searchString: String
) : RemoteMediator<Int, ImageData>() {
    private val imageDataDao = db.imageDataDao()
    private val remoteKeysDao: RemoteKeysDao = db.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageData>
    ): MediatorResult {
        try {
            val page = when (loadType) {
                REFRESH -> 1
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {

                    val currentPage = state.pages
                        .lastOrNull { it.data.isNotEmpty() }
                        ?.data?.lastOrNull()
                        ?.let { image -> remoteKeysDao.remoteKeysByRepoId(image.id) }

                    if (currentPage?.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    currentPage.nextKey
                }
            }

            val response = api.searchImages(
                page = page,
                query = searchString,
                perPage = when (loadType) {
                    REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            )
            response?.let {
                val items = it.data
                db.withTransaction {
                    if (loadType == REFRESH) {
                        imageDataDao.delete()
                        remoteKeysDao.delete()
                    }
                    val isEndOfList = it.data.isEmpty() || it.total_count == 0
                    val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                    val nextKey = if (isEndOfList) null else page + 1
                    val keys = items.map { image ->
                        RemoteKeys(repoId = image.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    remoteKeysDao.insertAll(keys)
                    imageDataDao.insertAll(items)

                }
                return MediatorResult.Success(endOfPaginationReached = items.isEmpty())
            }
            return MediatorResult.Error(IOException())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}

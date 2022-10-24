package com.geo.galleryapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.geo.galleryapp.api.GalleryAPI
import com.geo.galleryapp.db.GalleryDb
import com.geo.galleryapp.db.RemoteKeysDao
import com.geo.galleryapp.models.ImageData
import com.geo.galleryapp.models.RemoteKeys
import com.geo.galleryapp.other.Constants.DEFAULT_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class ImageListMediator(
    private val db: GalleryDb,
    private val api: GalleryAPI,
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

//            if (page > 2) {
//                return MediatorResult.Success(endOfPaginationReached = true)
//            }

            val response = api.searchImages(
                page = page,
                query = searchString,
                perPage = when (loadType) {
                    REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            )
            response.body()?.let {
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
            return MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
    /*override suspend fun load(
        loadType: LoadType, state: PagingState<Int, ImageData>
    ): MediatorResult {

        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = api.searchImages(
                page = page,
                query = searchString,
                perPage = when (loadType) {
                    REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                })
            val isEndOfList = response.body() == null || response.body()?.data.isNullOrEmpty()
            response.body()?.let {
                db.withTransaction {
                    // clear all tables in the database
                    if (loadType == REFRESH) {
                        imageDataDao.delete()
                        remoteKeysDao.delete()
                    }
                    val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                    val nextKey = if (isEndOfList) null else page + 1
                    val keys = it.data.map {
                        RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    imageDataDao.insertAll(it.data)
                    remoteKeysDao.insertAll(keys)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    *//**
     * this returns the page key or the final end of list success result
     *//*

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, ImageData>): Any? {
        return when (loadType) {
            REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.nextKey
            }
            PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                //end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }

    *//**
     * get the last remote key inserted which had the data
     *//*
    private suspend fun getLastRemoteKey(state: PagingState<Int, ImageData>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { image -> remoteKeysDao.remoteKeysByRepoId(image.id) }
    }

    *//**
     * get the first remote key inserted which had the data
     *//*
    private suspend fun getFirstRemoteKey(state: PagingState<Int, ImageData>): RemoteKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { image -> remoteKeysDao.remoteKeysByRepoId(image.id) }
    }

    *//**
     * get the closest remote key inserted which had the data
     *//*
    private suspend fun getClosestRemoteKey(state: PagingState<Int, ImageData>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                remoteKeysDao.remoteKeysByRepoId(repoId)
            }
        }
    }*/
}

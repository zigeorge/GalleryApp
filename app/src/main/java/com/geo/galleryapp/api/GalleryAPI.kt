package com.geo.galleryapp.api

import com.geo.galleryapp.models.ImageSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GalleryAPI {

    @GET("/v2/images/search")
    suspend fun searchImages(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("key") query: String
    ): Response<ImageSearchResponse?>

}
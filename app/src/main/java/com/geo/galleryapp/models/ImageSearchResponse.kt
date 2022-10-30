package com.geo.galleryapp.models

data class ImageSearchResponse(
    val data: List<ImageData>,
    val page: Int,
    val per_page: Int,
    val search_id: String,
    val total_count: Int,
    val message: String? = null
)
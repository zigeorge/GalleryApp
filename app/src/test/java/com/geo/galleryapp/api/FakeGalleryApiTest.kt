package com.geo.galleryapp.api

import com.geo.galleryapp.models.ImageData
import com.geo.galleryapp.models.ImageSearchResponse

class FakeGalleryApiTest: GalleryApi {

    companion object {
        private var index = 0
        var error = false
    }

    private var list = ArrayList<ImageData>()

    fun addImageDataList(imageList: List<ImageData>) {
        list.addAll(imageList)
    }

    override suspend fun searchImages(
        perPage: Int,
        page: Int,
        query: String
    ): ImageSearchResponse? {
        if (error) return null
        val data = ArrayList<ImageData>()
        var counter = 0
        if (page == 1) {
            index = 0
        }
        if (page*perPage > list.size) {
            return ImageSearchResponse(
                data = emptyList(),
                page = page,
                per_page = perPage,
                search_id = "",
                total_count = 0,
                message = "Too Many Results: Please provide more query parameters to narrow your search."
            )
        }
        while (counter < perPage && index < list.size) {
            data.add(list[index])
            index++
            counter++
        }
        return ImageSearchResponse(
            data = data,
            per_page = perPage,
            page = page,
            search_id = "cbabc182-ce69-4628-a4e9-f00711ec156d",
            total_count = list.size,
        )
    }

    fun reset() {
        index = 0
        error = false
        list = ArrayList()
    }
}

/*
* {
    "page": 100,
    "per_page": 500,
    "total_count": 0,
    "search_id": "",
    "data": [],
    "message": "Too Many Results: Please provide more query parameters to narrow your search."
}
* */


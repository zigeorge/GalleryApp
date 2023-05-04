package com.geo.galleryapp.repository

import com.geo.galleryapp.models.ImageData
import com.geo.galleryapp.models.RemoteKeys

class RemoteKeysFactoryAndroidTest {
    private var defaultRepoId = 1994710325

    private fun create(id: Int, key: Int) : RemoteKeys {
        val next = if (key == 100) {
            null
        } else key + 1
        val prev = if (key == 1) {
            null
        } else key - 1
        return RemoteKeys(
            repoId = "$id",
            nextKey = next,
            prevKey = prev
        )
    }

    fun fakeKeys(): List<RemoteKeys> {
        val list = ArrayList<RemoteKeys>()
        var currentKey = 1
        repeat(100) {
            list.add(create(defaultRepoId++, currentKey++))
        }
        return list
    }
}
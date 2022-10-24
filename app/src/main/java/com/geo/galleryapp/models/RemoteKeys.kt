package com.geo.galleryapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.geo.galleryapp.other.Constants.REMOTE_KEYS

@Entity(
    tableName = REMOTE_KEYS
)
data class RemoteKeys (
    @PrimaryKey val repoId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
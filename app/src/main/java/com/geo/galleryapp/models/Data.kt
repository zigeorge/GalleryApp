package com.geo.galleryapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.geo.galleryapp.other.Constants.GALLERY_TABLE

@Entity(
    tableName = GALLERY_TABLE
)
data class Data(
    val aspect: Double,
    val assets: Assets,
    val contributor: Contributor,
    val description: String,
    val has_model_release: Boolean,
    @PrimaryKey
    val id: String,
    val image_type: String,
    val media_type: String
)
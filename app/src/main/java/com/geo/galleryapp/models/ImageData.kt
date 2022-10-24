package com.geo.galleryapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.geo.galleryapp.other.Constants.IMAGE_DATA
import java.io.Serializable

@Entity(
    tableName = IMAGE_DATA
)
data class ImageData(
    val aspect: Double,
    val assets: Assets,
    val contributor: Contributor,
    val description: String,
    val has_model_release: Boolean,
    @PrimaryKey
    val id: String,
    val image_type: String,
    val media_type: String
): Serializable {
    var indexInResponse: Int = -1
}
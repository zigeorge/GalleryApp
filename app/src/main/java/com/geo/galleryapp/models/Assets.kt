package com.geo.galleryapp.models

import java.io.Serializable

data class Assets(
    val huge_thumb: Thumb,
    val large_thumb: Thumb,
    val preview: Thumb,
    val preview_1000: Thumb,
    val preview_1500: Thumb,
    val small_thumb: Thumb
): Serializable
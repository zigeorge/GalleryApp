package com.geo.galleryapp.models

import java.io.Serializable

data class Assets(
    val huge_thumb: Thumb,
    val large_thumb: Thumb,
    val preview: Preview,
    val preview_1000: Preview,
    val preview_1500: Preview,
    val small_thumb: Thumb
): Serializable
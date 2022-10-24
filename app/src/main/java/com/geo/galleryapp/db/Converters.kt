package com.geo.galleryapp.db

import androidx.room.TypeConverter
import com.geo.galleryapp.models.*
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromAssets(assets: Assets): String {
        return Gson().toJson(assets)
    }

    @TypeConverter
    fun toAssets(assets: String): Assets {
        return Gson().fromJson(assets, Assets::class.java)
    }

    @TypeConverter
    fun fromThumb(thumb: Thumb): String {
        return Gson().toJson(thumb)
    }

    @TypeConverter
    fun toThumb(thumb: String): Thumb {
        return Gson().fromJson(thumb, Thumb::class.java)
    }

    @TypeConverter
    fun fromContributor(contributor: Contributor): String {
        return Gson().toJson(contributor)
    }

    @TypeConverter
    fun toContributor(contributor: String): Contributor {
        return Gson().fromJson(contributor, Contributor::class.java)
    }
}
package com.geo.galleryapp.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geo.galleryapp.models.ImageData
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(imageList: List<ImageData>)

    @Query("SELECT * FROM image_data WHERE description LIKE :searchString ORDER BY indexInResponse ASC")
    fun galleryImages(searchString: String): PagingSource<Int, ImageData>

    @Query("SELECT * FROM image_data")
    fun galleryHudaiImages(): Flow<List<ImageData>>

    @Query("DELETE FROM image_data")
    suspend fun delete()
}
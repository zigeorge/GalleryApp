package com.geo.galleryapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geo.galleryapp.models.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE repoId = :id")
    suspend fun remoteKeysByRepoId(id: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun delete()
}
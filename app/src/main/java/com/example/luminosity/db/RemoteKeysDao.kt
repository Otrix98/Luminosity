package com.example.luminosity.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.luminosity.models.Photo

@Dao
interface RemoteKeysDao {

    @Query("SELECT * FROM unsplash_remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeys(id: String): FeedRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<FeedRemoteKeys>)

    @Query("DELETE FROM unsplash_remote_keys_table")
    suspend fun deleteAllRemoteKeys()
}
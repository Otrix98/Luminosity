package com.example.luminosity.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.luminosity.models.Photo


@Dao
interface PhotoDao {

    @Query("SELECT * FROM unsplash_image_table")
    fun getAllImages(): PagingSource<Int, Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImages(images: List<Photo>)

    @Query("DELETE FROM unsplash_image_table ")
    suspend fun deleteAllImages()
}
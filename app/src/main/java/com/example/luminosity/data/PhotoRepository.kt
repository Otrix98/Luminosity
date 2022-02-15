package com.example.luminosity.data

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.luminosity.data.Constants.ITEMS_PER_PAGE
import com.example.luminosity.db.UnsplashDatabase
import com.example.luminosity.models.Photo
import com.example.luminosity.models.PhotoDetails
import com.example.luminosity.networking.UnsplashApi
import com.example.luminosity.paging.FeedRemoteMediator
import com.example.luminosity.paging.SearchRemoteMediator
import com.skillbox.github.utils.haveQ
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val unsplashDatabase: UnsplashDatabase,
    private val context: Application) {
    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    @ExperimentalPagingApi
    fun getSearchResultStream(query: String): Flow<PagingData<Photo>> {
        if (query == DEFAULT_QUERY) {
            val pagingSourceFactory = { unsplashDatabase.unsplashImageDao().getAllImages() }
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
                remoteMediator = FeedRemoteMediator(
                    unsplashApi = unsplashApi,
                    unsplashDatabase = unsplashDatabase
                ),
                pagingSourceFactory = pagingSourceFactory
            ).flow
        } else {
            val pagingSourceFactory = { unsplashDatabase.unsplashImageDao().getAllImages() }
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
                remoteMediator = SearchRemoteMediator(
                    query = query,
                    api = unsplashApi,
                    database = unsplashDatabase
                ),
                pagingSourceFactory = pagingSourceFactory
            ).flow
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }

    suspend fun postLikeToPhoto(id: String): Int {
        val response = unsplashApi.postLikeToPhoto(id)
        return response.code()
    }

    suspend fun deleteLikeToPhoto(id: String): Int {
        val response = unsplashApi.deleteLikeToPhoto(id)
        return response.code()
    }

    suspend fun getPhotoDetails(id: String): PhotoDetails {
        return withContext(SupervisorJob()+ Dispatchers.IO) {
            unsplashApi.getPhotoDetails(id)
        }
    }

    private suspend fun downloadImage(url: String, uri: Uri): Int {
        var code = 404
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            try {
                if (unsplashApi.isFileExist(url).code() == 200) {
                    code = 200
                    unsplashApi.getFile(url)
                        .byteStream()
                        .use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                } else {
                    code = 404
                    return@use
                }
            } catch (t: Throwable) {
                code = 404
                return@use
            }
        }
        return code
    }

    suspend fun saveImage( name: String, url: String) {
        var code = 404
        withContext(Dispatchers.IO) {
            val imageUri = saveImageDetails(name, url)
            if (imageUri != null) {
                code = downloadImage(url, imageUri)
            }
            if (imageUri != null && code == 200) {
                makeImageVisible(imageUri)
            }
        }
    }

    private fun saveImageDetails(name: String, url: String): Uri? {
        if (name == "" || name.isBlank()) return null
        val value = if (haveQ()) {
            MediaStore.VOLUME_EXTERNAL_PRIMARY
        } else {
            MediaStore.VOLUME_EXTERNAL
        }
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(url)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)

        val imageCollectionUri = MediaStore.Images.Media.getContentUri(value)
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            if (haveQ()) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }
        return context.contentResolver.insert(imageCollectionUri, imageDetails)!!
    }

    private fun makeImageVisible(imageUri: Uri) {
        if (haveQ().not()) return
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.IS_PENDING, 0)
        }
        context.contentResolver.update(imageUri, imageDetails, null, null)
    }
}

private const val DEFAULT_QUERY = ""
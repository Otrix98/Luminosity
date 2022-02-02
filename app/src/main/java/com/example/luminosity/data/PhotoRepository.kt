package com.example.luminosity.data

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.luminosity.data.Constants.ITEMS_PER_PAGE
import com.example.luminosity.db.UnsplashDatabase
import com.example.luminosity.models.CollectionPhoto
import com.example.luminosity.models.DetailUserProfile
import com.example.luminosity.models.Photo
import com.example.luminosity.models.UserProfile
import com.example.luminosity.networking.UnsplashApi
import com.example.luminosity.paging.RemoteMediator
import com.example.luminosity.paging.SearchPagingSource
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

    @ExperimentalPagingApi
    fun getListPhoto(): Flow<PagingData<Photo>> {
        val pagingSourceFactory = { unsplashDatabase.unsplashImageDao().getAllImages() }
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = RemoteMediator(
                unsplashApi = unsplashApi,
                unsplashDatabase = unsplashDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getListSearchPhoto(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                SearchPagingSource(unsplashApi = unsplashApi, query = query)
            }
        ).flow
    }

    suspend fun getListCollections(page: Int): List<CollectionPhoto> {
        return withContext(SupervisorJob()+ Dispatchers.IO) {
            unsplashApi.getListCollections(page)
        }
    }

    suspend fun getListPhotosInCollection(idCollection: String, page: Int): List<Photo> {
        return withContext(SupervisorJob()+ Dispatchers.IO) {
            unsplashApi.getListPhotosInCollection(idCollection,page)
        }
    }

    suspend fun getUserProfile(): UserProfile {
        return withContext(SupervisorJob()+ Dispatchers.IO) {
            unsplashApi.getUserProfile()
        }
    }

    suspend fun getDetailProfile(userName: String): DetailUserProfile {
        return withContext(SupervisorJob()+ Dispatchers.IO) {
            unsplashApi.getDetailUserProfile(userName)
        }
    }

    suspend fun getListLikePhoto(userName: String, page: Int): List<Photo> {
        return withContext(SupervisorJob()+ Dispatchers.IO) {
            unsplashApi.getListUsersLikesPhotos(userName,page)
        }
    }

    suspend fun postLikeToPhoto(id: String): Int {
        val response = unsplashApi.postLikeToPhoto(id)
        return response.code()
    }

    suspend fun deleteLikeToPhoto(id: String): Int {
        val response = unsplashApi.deleteLikeToPhoto(id)
        return response.code()
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
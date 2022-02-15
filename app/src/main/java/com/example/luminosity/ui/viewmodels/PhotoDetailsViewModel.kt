package com.example.luminosity.ui.viewmodels

import android.widget.Toast
import androidx.lifecycle.*
import com.example.luminosity.data.PhotoRepository
import com.example.luminosity.models.PhotoDetails
import com.example.luminosity.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val photosMutable = MutableLiveData<PhotoDetails>()
    val photo: LiveData<PhotoDetails>
        get() = photosMutable

    private val showProgressBarMutable = MutableLiveData(false)
    val progressBarVisible: LiveData<Boolean>
        get() = showProgressBarMutable

    private val serverConnectErrorEvent = SingleLiveEvent<Boolean>()
    val serverConnectError: LiveData<Boolean>
        get() = serverConnectErrorEvent

    suspend fun postLikeToPhoto(id: String, scope: CoroutineScope) {
        val photo = photosMutable.value
        var code: Int? = null
        val job = scope.launch(Dispatchers.IO) {
            code = photoRepository.postLikeToPhoto(id)
            if (code == 201) {
                if (photo != null) {
                    photo.liked = true
                    photosMutable.postValue(photo!!)
                }
            } else {
                serverConnectErrorEvent.postValue(true)
            }
        }
        job.join()
    }

    suspend fun deleteLikeToPhoto(id: String, scope: CoroutineScope) {
        val photo = photosMutable.value
        var code: Int? = null
        val job = scope.launch(Dispatchers.IO) {
            code = photoRepository.deleteLikeToPhoto(id)
            if (code == 201) {
                if (photo != null) {
                    photo.liked = false
                    photosMutable.postValue(photo!!)
                }
            } else {
                serverConnectErrorEvent.postValue(true)
            }
        }
        job.join()
    }

    suspend fun getPhotoDetails(id: String) {
        viewModelScope.launch {
            try {
                showProgressBar()
                photoRepository.getPhotoDetails(id)
                    .also { receivedPhoto ->
                        hideProgressBar()
                        photosMutable.postValue(receivedPhoto)
                    }
            } catch (t: Throwable) {
                hideProgressBar()
                println(id)
                println(t.message)
                serverConnectErrorEvent.postValue(true)
            }
        }
    }

    fun showProgressBar() {
        showProgressBarMutable.postValue(true)
    }

    fun hideProgressBar() {
        showProgressBarMutable.postValue(false)
    }

}
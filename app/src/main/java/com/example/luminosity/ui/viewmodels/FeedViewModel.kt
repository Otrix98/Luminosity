package com.example.luminosity.ui.viewmodels

import android.os.Parcelable
import androidx.lifecycle.*
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import com.example.luminosity.adapters.*
import com.example.luminosity.data.PhotoRepository
import com.example.luminosity.models.CollectionPhoto
import com.example.luminosity.models.Photo
import com.example.luminosity.utils.SingleLiveEvent
import com.skillbox.github.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val DEFAULT_QUERY = ""

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {


    private val showProgressBarMutable = MutableLiveData(false)
    val progressBarVisible: LiveData<Boolean>
        get() = showProgressBarMutable

    private val serverConnectErrorEvent = SingleLiveEvent<Boolean>()
    val serverConnectError: LiveData<Boolean>
        get() = serverConnectErrorEvent

    suspend fun postLikeToPhoto(id: String, scope: CoroutineScope): Int? {
        var code: Int? = null
        val job = scope.launch(Dispatchers.IO) {
            code = photoRepository.postLikeToPhoto(id)
        }
        job.join()
        return code
    }

    suspend fun deleteLikeToPhoto(id: String, scope: CoroutineScope): Int? {
        var code: Int? = null
        val job = scope.launch(Dispatchers.IO) {
            code = photoRepository.deleteLikeToPhoto(id)
        }
        job.join()
        return code
    }

    private val _query = MutableStateFlow(DEFAULT_QUERY)

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val searchResults: StateFlow<PagingData<Photo>>
        get() = _searchResults
    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    private val _searchResults = _query
        .flatMapLatest { photoRepository.getListPhoto() }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    }
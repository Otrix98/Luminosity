package com.example.luminosity.ui.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.example.luminosity.data.PhotoRepository
import com.example.luminosity.models.Photo
import com.example.luminosity.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

//private const val DEFAULT_QUERY = ""

@ExperimentalPagingApi
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

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
//
//    private val _query = MutableStateFlow(DEFAULT_QUERY)
//
//    @ExperimentalCoroutinesApi
//    @ExperimentalPagingApi
//    val searchResults: StateFlow<PagingData<Photo>>
//        get() = _searchResults
//    @ExperimentalCoroutinesApi
//    @ExperimentalPagingApi
//    private val _searchResults = _query
//        .flatMapLatest { photoRepository.getListPhoto() }
//        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())


    /**
     * Stream of immutable states representative of the UI.
     */
    val state: StateFlow<UiState>

    val pagingDataFlow: Flow<PagingData<Photo>>

    /**
     * Processor of side effects from the UI which in turn feedback into [state]
     */
    val accept: (UiAction) -> Unit

    init {
        val initialQuery: String = savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        val lastQueryScrolled: String = savedStateHandle.get(LAST_QUERY_SCROLLED) ?: DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = initialQuery)) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(currentQuery = lastQueryScrolled)) }

        pagingDataFlow = searches
            .flatMapLatest { searchPhoto(queryString = it.query) }
            .cachedIn(viewModelScope)

        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            UiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                // If the search query matches the scroll query, the user has scrolled
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = UiState()
            )

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value.query
        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.lastQueryScrolled
        super.onCleared()
    }

    @ExperimentalPagingApi
    private fun searchPhoto(queryString: String): Flow<PagingData<Photo>> =
        photoRepository.getSearchResultStream(queryString)
}

sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()
}

data class UiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)

private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = ""

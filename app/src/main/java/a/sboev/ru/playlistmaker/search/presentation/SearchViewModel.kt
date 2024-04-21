package a.sboev.ru.playlistmaker.search.presentation

import a.sboev.ru.playlistmaker.search.domain.api.HistoryInteractor
import a.sboev.ru.playlistmaker.search.domain.api.TrackInteractor
import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.search.ui.models.TracksState
import a.sboev.ru.playlistmaker.utils.debounce
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class SearchViewModel(application: Application): AndroidViewModel(application) {

    private var lastSearchRequest: String = ""
    private val trackInteractor: TrackInteractor by inject(TrackInteractor::class.java)
    private val historyInteractor: HistoryInteractor by inject(HistoryInteractor::class.java)
    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData
    private val searchHistoryListLiveData = MutableLiveData<MutableList<Track>?>()
    fun observeHistoryList(): LiveData<MutableList<Track>?> = searchHistoryListLiveData
    private val onSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { searchString ->
        makeRequest(searchString)
    }

    fun searchDebounce(searchString: String) {
        if (lastSearchRequest == searchString)
            return
        lastSearchRequest = searchString
        onSearchDebounce(lastSearchRequest)
    }

    private fun makeRequest(newSearchString: String) {
        if (newSearchString.isNotEmpty()) {
            renderState(TracksState.Loading)

            viewModelScope.launch {
                trackInteractor.searchTracks(newSearchString).collect { pair ->
                    processResult(pair.first, pair.second)
                }
            }

        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null)
            tracks.addAll(foundTracks)

        when {
            errorMessage != null -> {
                renderState(
                    TracksState.Error(errorMessage)
                )
            }
            tracks.isEmpty() -> {
                renderState(
                    TracksState.Empty("")
                )
            }
            else -> {
                renderState(
                    TracksState.Content(tracks)
                )
            }
        }
    }

    fun writeSearchHistory(track: Track) {
        val searchHistoryList = searchHistoryListLiveData.value
        if (searchHistoryList?.contains(track) == true) {
            searchHistoryList.remove(track)
            searchHistoryList.add(0, track)
        } else if (searchHistoryList?.size == SEARCH_HISTORY_LIMIT) {
            searchHistoryList.removeAt(searchHistoryList.lastIndex)
            searchHistoryList.add(0, track)
        } else {
            searchHistoryList?.add(0, track)
        }
        searchHistoryList?.toList()?.let { historyInteractor.write(it) }
        searchHistoryListLiveData.postValue(searchHistoryList)
    }

    fun readSearchHistory() {
        searchHistoryListLiveData.postValue(historyInteractor.read().toMutableList())
    }

    fun clearSearchHistory() {
        historyInteractor.clear()
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SEARCH_HISTORY_LIMIT = 10
    }
}
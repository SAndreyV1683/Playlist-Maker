package a.sboev.ru.playlistmaker.search.presentation

import a.sboev.ru.playlistmaker.creators.HistoryInteractorCreator
import a.sboev.ru.playlistmaker.creators.TrackInteractorCreator
import a.sboev.ru.playlistmaker.search.domain.api.TrackInteractor
import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.search.ui.models.TracksState
import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class SearchViewModel(application: Application): AndroidViewModel(application) {

    private var lastSearchRequest: String = ""
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { makeRequest(lastSearchRequest) }
    private val trackInteractor = TrackInteractorCreator.provideTrackInteractor()
    private val historyInteractor = HistoryInteractorCreator.provideHistoryInteractor()
    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData
    private val searchHistoryListLiveData = MutableLiveData<MutableList<Track>>()
    fun observeHistoryList(): LiveData<MutableList<Track>> = searchHistoryListLiveData


    fun searchDebounce(searchString: String) {
        lastSearchRequest = searchString
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun makeRequest(newSearchString: String) {
        if (newSearchString.isNotEmpty()) {
            renderState(TracksState.Loading)
            trackInteractor.searchTracks(
                newSearchString, object : TrackInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
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
                }
            )
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

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SEARCH_HISTORY_LIMIT = 10
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}
package a.sboev.ru.playlistmaker.library.presentation.viewmodels

import a.sboev.ru.playlistmaker.library.domain.api.DatabaseInteractor
import a.sboev.ru.playlistmaker.library.presentation.LibState
import a.sboev.ru.playlistmaker.search.domain.models.Track
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FeaturedTracksViewModel(private val databaseInteractor: DatabaseInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<LibState>(LibState.Empty)
    fun observeState(): LiveData<LibState> = stateLiveData

    fun fillData() {
        stateLiveData.value = LibState.Loading
        viewModelScope.launch {
            databaseInteractor
                .getTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            stateLiveData.value = LibState.Empty
        } else {
            stateLiveData.value = LibState.Content(tracks)
        }
    }
}
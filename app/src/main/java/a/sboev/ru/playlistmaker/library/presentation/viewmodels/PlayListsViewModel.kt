package a.sboev.ru.playlistmaker.library.presentation.viewmodels

import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.library.presentation.LibState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlayListsViewModel(private val playlistDatabaseInteractor: PlaylistDatabaseInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<LibState>(LibState.Empty)
    fun observeState(): LiveData<LibState> = stateLiveData

    fun getPlaylists() {
        viewModelScope.launch {
            playlistDatabaseInteractor.getPlaylists().collect { playlists ->
                processResult(playlists)
            }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            stateLiveData.value = LibState.Empty
        } else {
            stateLiveData.value = LibState.Content(playlists)
        }
    }
}
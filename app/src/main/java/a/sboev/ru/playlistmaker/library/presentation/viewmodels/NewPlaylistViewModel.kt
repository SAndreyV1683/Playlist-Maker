package a.sboev.ru.playlistmaker.library.presentation.viewmodels

import a.sboev.ru.playlistmaker.library.domain.api.FilesInteractor
import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPlaylistViewModel(
    private val playlistDatabaseInteractor: PlaylistDatabaseInteractor,
    private val filesInteractor: FilesInteractor
): ViewModel() {
    fun createNewPlayList(
        playlistName: String,
        playlistDescription: String,
        playlistImageUri: String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistDatabaseInteractor.insertPlaylist(Playlist(
                    id = null,
                    name = playlistName,
                    description = playlistDescription,
                    uri = playlistImageUri,
                    tracksIdList = emptyList(),
                    tracksCount = 0
                ))
            }
        }
    }

    fun saveImageToPrivateStorage(uri: Uri, name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                filesInteractor.saveImage(uri, name)
            }
        }
    }
}
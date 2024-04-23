package a.sboev.ru.playlistmaker.library.presentation.viewmodels

import a.sboev.ru.playlistmaker.library.presentation.LibState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayListsViewModel: ViewModel() {

    private val stateLiveData = MutableLiveData<LibState>(LibState.Empty)
    fun observeState(): LiveData<LibState> = stateLiveData
}
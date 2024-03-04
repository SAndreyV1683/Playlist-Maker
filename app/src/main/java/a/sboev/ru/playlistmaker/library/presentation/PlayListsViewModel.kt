package a.sboev.ru.playlistmaker.library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayListsViewModel: ViewModel() {

    private val stateLiveData = MutableLiveData<LibState>(LibState.Error)
    fun observeState(): LiveData<LibState> = stateLiveData
}
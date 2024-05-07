package a.sboev.ru.playlistmaker.library.domain.impl

import a.sboev.ru.playlistmaker.library.domain.api.FilesInteractor
import a.sboev.ru.playlistmaker.library.domain.api.FilesRepository
import android.net.Uri

class FilesInteractorImpl(private val filesRepository: FilesRepository): FilesInteractor {
    override suspend fun saveImage(uri: Uri, name: String): Uri {
        return filesRepository.saveImage(uri, name)
    }
}
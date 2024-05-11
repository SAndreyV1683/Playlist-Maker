package a.sboev.ru.playlistmaker.library.data.impl

import a.sboev.ru.playlistmaker.library.data.FilesHandler
import a.sboev.ru.playlistmaker.library.domain.api.FilesRepository
import android.net.Uri

class FilesRepositoryImpl(
    private val filesHandler: FilesHandler
): FilesRepository {
    override suspend fun saveImage(uri: Uri, name: String): Uri {
        return filesHandler.saveImage(uri, name)
    }
}
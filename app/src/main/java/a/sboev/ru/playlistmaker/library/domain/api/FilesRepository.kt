package a.sboev.ru.playlistmaker.library.domain.api

import android.net.Uri

interface FilesRepository {
    suspend fun saveImage(uri: Uri, name: String): Uri
}
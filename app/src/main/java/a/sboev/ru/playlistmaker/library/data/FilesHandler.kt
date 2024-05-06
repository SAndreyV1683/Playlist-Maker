package a.sboev.ru.playlistmaker.library.data

import android.net.Uri

interface FilesHandler {
    suspend fun saveImage(uri: Uri, name: String)
}
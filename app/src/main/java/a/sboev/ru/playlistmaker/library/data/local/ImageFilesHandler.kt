package a.sboev.ru.playlistmaker.library.data.local

import a.sboev.ru.playlistmaker.library.data.FilesHandler
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ImageFilesHandler(private val context: Context): FilesHandler {
    override suspend fun saveImage(uri: Uri, name: String): Uri {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PlayLists")
        if (!filePath.exists()) {
            filePath.mkdir()
        }
        val file = File(filePath, name)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri()
    }
}
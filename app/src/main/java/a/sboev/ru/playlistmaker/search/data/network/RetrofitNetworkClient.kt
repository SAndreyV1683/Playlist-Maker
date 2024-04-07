package a.sboev.ru.playlistmaker.search.data.network

import a.sboev.ru.playlistmaker.search.data.NetworkClient
import a.sboev.ru.playlistmaker.search.data.dto.Response
import a.sboev.ru.playlistmaker.search.data.dto.TrackSearchRequest
import a.sboev.ru.playlistmaker.utils.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(imdbService: ITunesApiService): NetworkClient {

    private val retrofitService = imdbService
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1}
        }

        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = retrofitService.search(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }
}
package a.sboev.ru.playlistmaker.search.data.network

import a.sboev.ru.playlistmaker.search.data.NetworkClient
import a.sboev.ru.playlistmaker.search.data.dto.Response
import a.sboev.ru.playlistmaker.search.data.dto.TrackSearchRequest
import a.sboev.ru.playlistmaker.utils.isConnected

class RetrofitNetworkClient: NetworkClient {

    private val retrofitService = ITunesApi.retrofitService
    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1}
        }
        return if (dto is TrackSearchRequest) {
            val response = retrofitService.search(dto.expression).execute()
            val body = response.body() ?: Response()
            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}
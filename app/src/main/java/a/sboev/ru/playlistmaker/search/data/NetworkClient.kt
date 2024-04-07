package a.sboev.ru.playlistmaker.search.data

import a.sboev.ru.playlistmaker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
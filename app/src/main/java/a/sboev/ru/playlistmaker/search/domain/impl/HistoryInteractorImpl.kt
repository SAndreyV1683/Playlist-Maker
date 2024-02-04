package a.sboev.ru.playlistmaker.search.domain.impl

import a.sboev.ru.playlistmaker.search.domain.api.HistoryInteractor
import a.sboev.ru.playlistmaker.search.domain.api.HistoryRepository
import a.sboev.ru.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(private val historyRepository: HistoryRepository): HistoryInteractor {
    override fun read(): List<Track> {
        return historyRepository.read()
    }

    override fun write(trackList: List<Track>) {
        historyRepository.write(trackList)
    }

    override fun clear() {
        historyRepository.clear()
    }
}
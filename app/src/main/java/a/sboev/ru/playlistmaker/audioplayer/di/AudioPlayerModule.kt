package a.sboev.ru.playlistmaker.audioplayer.di

import a.sboev.ru.playlistmaker.audioplayer.data.MyMediaPlayer
import a.sboev.ru.playlistmaker.audioplayer.data.Player
import a.sboev.ru.playlistmaker.audioplayer.data.PlayerRepositoryImpl
import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerInterActor
import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import a.sboev.ru.playlistmaker.audioplayer.domain.impl.PlayerInterActorImpl
import org.koin.dsl.module

val audioPlayerModule = module {

    factory<Player> {
        MyMediaPlayer()
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    factory<PlayerInterActor> {
        PlayerInterActorImpl(get())
    }

}
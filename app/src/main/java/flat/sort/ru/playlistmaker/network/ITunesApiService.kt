package flat.sort.ru.playlistmaker.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val baseUrl = "https://itunes.apple.com"
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface ITunesApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}

object ITunesApi {
    val retrofitService: ITunesApiService by lazy {
        retrofit.create(ITunesApiService::class.java)
    }
}

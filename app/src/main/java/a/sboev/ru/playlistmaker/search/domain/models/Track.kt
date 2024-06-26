package a.sboev.ru.playlistmaker.search.domain.models

import android.os.Parcel
import android.os.Parcelable


data class Track(
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    var timeToAddToFavorites: Long
): Parcelable {

    val artworkUrl512
        get() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(trackId)
        dest.writeString(trackName)
        dest.writeString(artistName)
        dest.writeLong(trackTimeMillis)
        dest.writeString(artworkUrl100)
        dest.writeString(previewUrl)
        dest.writeString(collectionName)
        dest.writeString(releaseDate)
        dest.writeString(primaryGenreName)
        dest.writeString(country)
        dest.writeLong(timeToAddToFavorites)
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }


}

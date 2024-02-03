package a.sboev.ru.playlistmaker.adapters

import a.sboev.ru.playlistmaker.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import a.sboev.ru.playlistmaker.models.Track
import a.sboev.ru.playlistmaker.utils.getDuration


class TracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    private val image = itemView.findViewById<ImageView>(R.id.album_image)
    private val trackName = itemView.findViewById<TextView>(R.id.track_name)
    private val artistName = itemView.findViewById<TextView>(R.id.singer_name)
    private val trackTime = itemView.findViewById<TextView>(R.id.track_duration)
    private val roundingRadius = 10

    fun bind(track: Track) {
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(roundingRadius))
            .into(image)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.getDuration()
    }

}
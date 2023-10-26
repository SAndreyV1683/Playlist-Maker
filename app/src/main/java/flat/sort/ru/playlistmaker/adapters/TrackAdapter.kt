package flat.sort.ru.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import flat.sort.ru.playlistmaker.R
import flat.sort.ru.playlistmaker.models.Track

class TrackAdapter(private val tracks: ArrayList<Track>): RecyclerView.Adapter<TracksViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
    }
}
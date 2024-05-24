package a.sboev.ru.playlistmaker.search.ui.adapters

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.search.domain.models.Track
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class TrackAdapter(
    private val onItemClickListener: OnItemClickListener
): RecyclerView.Adapter<TracksViewHolder>()  {
    interface OnItemClickListener {
        fun onLongClick(track: Track)
        fun onClick(track: Track)
    }

    var tracks = mutableListOf<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    fun refresh() {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            onItemClickListener.onLongClick(tracks[position])
            true
        }
    }
}
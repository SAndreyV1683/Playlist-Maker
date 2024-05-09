package a.sboev.ru.playlistmaker.library.ui.newplaylist.adapters

import a.sboev.ru.playlistmaker.App
import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.databinding.PlaylistItemHorizontalBinding
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.search.domain.models.Track
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlaylistItemHorizontalAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PlaylistItemHorizontalAdapter.PlaylistItemHorizontalViewHolder>() {
    var playlists = mutableListOf<Playlist>()

    fun interface OnItemClickListener {
        fun onClick(playlist: Playlist)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistItemHorizontalViewHolder {
        val binding = PlaylistItemHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistItemHorizontalViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistItemHorizontalViewHolder, position: Int) {
        with(holder) {
            with(playlists[position]) {
                binding.playlistNameTv.text = this.name
                if (this.tracksCount == 0)
                    binding.playlistTracksCount.text = holder.itemView.context.getString(R.string.playlist_tracks_count, this.tracksCount.toString())
                else if (this.tracksCount == 1)
                    binding.playlistTracksCount.text = holder.itemView.context.getText(R.string.playlist_one_track_count)
                else if (this.tracksCount < 5)
                    binding.playlistTracksCount.text = holder.itemView.context.getString(R.string.playlist_less_than_5_tracks_count, this.tracksCount.toString())
                else
                    binding.playlistTracksCount.text = holder.itemView.context.getString(R.string.playlist_tracks_count, this.tracksCount.toString())

                Glide.with(holder.itemView.context)
                    .load(Uri.parse(this.uri))
                    .placeholder(R.drawable.ic_placeholder)
                    .transform(RoundedCorners(PlaylistItemAdapter.ROUNDING_RADIUS))
                    .into(binding.playlistCoverIv)
            }
            this.itemView.setOnClickListener {
                onItemClickListener.onClick(playlists[position])
            }
        }
    }

    fun refresh() {
        notifyDataSetChanged()
    }


    inner class PlaylistItemHorizontalViewHolder(val binding: PlaylistItemHorizontalBinding): RecyclerView.ViewHolder(binding.root)
}
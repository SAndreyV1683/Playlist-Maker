package a.sboev.ru.playlistmaker.library.ui.newplaylist.adapters

import a.sboev.ru.playlistmaker.App
import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.databinding.PlaylistItemBinding
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlaylistItemAdapter: RecyclerView.Adapter<PlaylistItemViewHolder>() {

    var playlists = mutableListOf<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemViewHolder {
        val binding = PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {
        with(holder) {
            with(playlists[position]) {
                binding.playlistNameTv.text = this.name
                if (this.tracksCount == 0)
                    binding.playlistTracksCount.text = App.INSTANCE.getString(R.string.playlist_tracks_count, this.tracksCount.toString())
                else if (this.tracksCount == 1)
                    binding.playlistTracksCount.text = holder.itemView.context.getText(R.string.playlist_one_track_count)
                else if (this.tracksCount < 5)
                    binding.playlistTracksCount.text = holder.itemView.context.getString(R.string.playlist_less_than_5_tracks_count, this.tracksCount.toString())
                else
                    binding.playlistTracksCount.text = holder.itemView.context.getString(R.string.playlist_tracks_count, this.tracksCount.toString())

                Glide.with(holder.itemView.context)
                    .load(Uri.parse(this.uri))
                    .placeholder(R.drawable.ic_placeholder)
                    .transform(RoundedCorners(ROUNDING_RADIUS))
                    .into(binding.playlistCoverIv)
            }
        }
    }

    fun refresh() {
        notifyDataSetChanged()
    }

    companion object {
        const val ROUNDING_RADIUS = 5
    }
}
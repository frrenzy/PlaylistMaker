package com.example.playlistmaker.player.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistsSmallAdapter(val onClickListener: SmallPlaylistClickListener? = null) :
    RecyclerView.Adapter<PlaylistSmallViewHolder>() {
    var playlists: List<Playlist> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSmallViewHolder =
        PlaylistSmallViewHolder.from(parent)

    override fun onBindViewHolder(holder: PlaylistSmallViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { onClickListener?.onClick(playlist) }
    }

    override fun getItemCount() = playlists.size

    fun interface SmallPlaylistClickListener {
        fun onClick(playlist: Playlist)
    }
}

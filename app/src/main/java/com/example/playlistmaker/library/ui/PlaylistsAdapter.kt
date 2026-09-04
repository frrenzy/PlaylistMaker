package com.example.playlistmaker.library.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistsAdapter(val onClickListener: PlaylistClickListener? = null) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    var playlists: List<Playlist> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder =
        PlaylistViewHolder.from(parent)

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { onClickListener?.onClick(playlist) }
    }

    override fun getItemCount(): Int = playlists.size

    fun interface PlaylistClickListener {
        fun onClick(playlist: Playlist)
    }
}

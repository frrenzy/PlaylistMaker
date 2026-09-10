package com.example.playlistmaker.common.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.domain.models.Track

class TrackAdapter(
    val onLongClickListener: TrackLongClickListener? = null,
    val onClickListener: TrackClickListener? = null,
) :
    RecyclerView.Adapter<TrackViewHolder>() {
    var tracks: List<Track> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder.from(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.apply {
            setOnClickListener { onClickListener?.onClick(track) }
            setOnLongClickListener {
                onLongClickListener?.onClick(track)
                true
            }
        }
    }

    override fun getItemCount() = tracks.size

    fun interface TrackClickListener {
        fun onClick(track: Track)
    }

    fun interface TrackLongClickListener {
        fun onClick(track: Track)
    }
}

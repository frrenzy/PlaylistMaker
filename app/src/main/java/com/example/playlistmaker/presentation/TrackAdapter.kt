package com.example.playlistmaker.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class TrackAdapter(val onClickListener: TrackClickListener? = null) :
    RecyclerView.Adapter<TrackViewHolder>() {
    var tracks: List<Track> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_card, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onClickListener?.onClick(tracks[position]) }
    }

    override fun getItemCount() = tracks.size

    fun interface TrackClickListener {
        fun onClick(track: Track)
    }
}
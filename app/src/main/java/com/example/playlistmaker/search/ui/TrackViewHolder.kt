package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackCardBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.dp

class TrackViewHolder(private val binding: TrackCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Track) {
        binding.apply {
            Glide.with(root)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.ic_placeholder_45)
                .centerCrop()
                .transform(RoundedCorners(TRACK_ART_CORNER_RADIUS.dp))
                .into(image)
            title.text = model.trackName
            author.text = model.artistName
            length.text = model.trackTime
        }
    }

    companion object {
        const val TRACK_ART_CORNER_RADIUS = 2

        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackCardBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }
}
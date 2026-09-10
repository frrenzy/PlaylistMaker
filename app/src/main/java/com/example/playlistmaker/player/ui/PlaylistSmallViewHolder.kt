package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.signature.ObjectKey
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistSmallCardBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.utils.getCoverImageFile
import com.example.playlistmaker.utils.ui.dp

class PlaylistSmallViewHolder(private val binding: PlaylistSmallCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        val coverImage = getCoverImageFile(binding.root.context, model.coverPath)

        binding.apply {
            Glide.with(root)
                .load(coverImage)
                .signature(ObjectKey(coverImage?.lastModified() ?: 0))
                .placeholder(R.drawable.ic_placeholder_45)
                .transform(CenterCrop(), RoundedCorners(PLAYLIST_COVER_CORNER_RADIUS.dp))
                .into(cover)
            name.text = model.name
            amount.text =
                binding.root.context.resources.getQuantityString(
                    R.plurals.playlist_track_amount,
                    model.amount,
                    model.amount,
                )
        }
    }

    companion object {
        const val PLAYLIST_COVER_CORNER_RADIUS = 8

        fun from(parent: ViewGroup): PlaylistSmallViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistSmallCardBinding.inflate(inflater, parent, false)
            return PlaylistSmallViewHolder(binding)
        }
    }
}

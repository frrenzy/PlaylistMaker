package com.example.playlistmaker.player.ui

import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.data.Constants
import com.example.playlistmaker.databinding.PlaylistSmallCardBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.utils.dp
import java.io.File

class PlaylistSmallViewHolder(private val binding: PlaylistSmallCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        val filePath =
            File(
                binding.root.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                Constants.COVERS_DIR,
            )
        val file = File(filePath, model.coverPath ?: "")

        binding.apply {
            Glide.with(root)
                .load(file)
                .placeholder(R.drawable.ic_placeholder_45)
                .transform(CenterCrop(), RoundedCorners(PLAYLIST_COVER_CORNER_RADIUS.dp))
                .into(cover)
            name.text = model.name
            amount.text =
                binding.root.context.getString(R.string.playlist_track_amount, model.amount)
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

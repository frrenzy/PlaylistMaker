package com.example.playlistmaker.track

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val image = itemView.findViewById<ImageView>(R.id.track_image)
    private val title = itemView.findViewById<TextView>(R.id.track_title)
    private val author = itemView.findViewById<TextView>(R.id.track_author)
    private val length = itemView.findViewById<TextView>(R.id.track_length)

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_image_45)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(image)
        title.text = model.trackName
        author.text = model.artistName
        length.text = model.trackTime
    }
}
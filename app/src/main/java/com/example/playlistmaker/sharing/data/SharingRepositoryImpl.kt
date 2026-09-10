package com.example.playlistmaker.sharing.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingRepositoryImpl(private val context: Context) : SharingRepository {
    override fun getShareAppLink(): String = context.getString(R.string.share_message)

    override fun getSupportEmailData(): EmailData = EmailData(
        arrayOf(context.getString(R.string.support_email)),
        context.getString(R.string.support_subject),
        context.getString(R.string.support_message)
    )

    override fun getTermsLink(): String = context.getString(R.string.agreement_link)

    override fun getPlaylistData(playlist: Playlist, tracks: List<Track>) = buildString {
        appendLine(playlist.name)
        playlist.description?.let { appendLine(it) }
        appendLine(
            context.resources.getQuantityString(
                R.plurals.playlist_track_amount,
                tracks.size,
                tracks.size,
            )
        )
        appendLine()

        tracks.forEachIndexed { index, track ->
            append("${index + 1}. ")
            append("${track.artistName} - ")
            append("${track.trackName} ")
            append("(${track.trackTime})")
            appendLine()
        }
    }
}

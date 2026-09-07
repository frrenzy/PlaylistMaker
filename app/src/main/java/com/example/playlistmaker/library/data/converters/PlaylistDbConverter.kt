package com.example.playlistmaker.library.data.converters

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.data.db.entities.PlaylistEntity
import com.example.playlistmaker.library.data.db.entities.TrackEntity
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity = with(playlist) {
        PlaylistEntity(
            name = name,
            description = description,
            coverPath = coverPath,
            amount = amount,
        )
    }

    fun map(playlist: PlaylistEntity): Playlist = with(playlist) {
        Playlist(
            id = playlistId,
            name = name,
            description = description,
            coverPath = coverPath,
            amount = amount,
        )
    }

    fun map(track: Track): TrackEntity = with(track) {
        TrackEntity(
            trackId = trackId,
            trackName = trackName,
            artistName = artistName,
            coverArtworkUrl = coverArtworkUrl,
            trackTimeMillis = trackTimeMillis,
            artworkUrl100 = artworkUrl100,
            collectionName = collectionName,
            country = country,
            primaryGenreName = primaryGenreName,
            previewUrl = previewUrl,
            releaseDate = releaseDate,
        )
    }

    fun map(track: TrackEntity): Track = with(track) {
        Track(
            trackId = trackId,
            trackName = trackName,
            artistName = artistName,
            coverArtworkUrl = coverArtworkUrl,
            trackTimeMillis = trackTimeMillis,
            artworkUrl100 = artworkUrl100,
            collectionName = collectionName,
            country = country,
            primaryGenreName = primaryGenreName,
            previewUrl = previewUrl,
            releaseDate = releaseDate,
            isFavourite = true,
        )
    }
}

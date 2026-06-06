package com.example.playlistmaker.history.data

import com.example.playlistmaker.history.domain.TracksHistoryRepository
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track

class TracksHistoryRepositoryImpl(private val historyClient: HistoryClient) :
    TracksHistoryRepository {
    override fun clear() {
        historyClient.clear()
    }

    override fun loadTracks(): List<Track> {
        val response = historyClient.getAll()
        return response.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl,
                coverArtworkUrl = it.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"),
                year = it.releaseDate?.substringBefore('-')
            )
        }
    }

    override fun addTrack(track: Track) {
        val dto = TrackDto(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
        historyClient.add(dto)
    }

    override fun isEmpty(): Boolean = historyClient.size == 0
}
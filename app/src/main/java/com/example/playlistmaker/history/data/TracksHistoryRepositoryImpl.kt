package com.example.playlistmaker.history.data

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.history.domain.TracksHistoryRepository
import com.example.playlistmaker.search.data.dto.TrackDto

class TracksHistoryRepositoryImpl(private val historyClient: HistoryClient) :
    TracksHistoryRepository {
    override fun clear() {
        historyClient.clear()
    }

    override suspend fun loadTracks(): List<Track> {
        val response = historyClient.getAll()

        return response.map {
            with(it) {
                Track(
                    trackId = trackId,
                    trackName = trackName,
                    artistName = artistName,
                    trackTimeMillis = trackTimeMillis,
                    artworkUrl100 = artworkUrl100,
                    collectionName = collectionName,
                    releaseDate = releaseDate,
                    primaryGenreName = primaryGenreName,
                    country = country,
                    previewUrl = previewUrl,
                    coverArtworkUrl = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"),
                )
            }
        }
    }

    override fun addTrack(track: Track) {
        val dto = with(track) {
            TrackDto(
                trackId = trackId,
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                artworkUrl100 = artworkUrl100,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl,
            )
        }
        historyClient.add(dto)
    }

    override fun isEmpty(): Boolean = historyClient.size == 0
}

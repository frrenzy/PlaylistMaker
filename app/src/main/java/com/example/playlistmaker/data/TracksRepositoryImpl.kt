package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(term: String): List<Track> {
        val response = networkClient.searchTracks(TracksSearchRequest(term))
        return if (response.resultCode == 200 && response is TracksSearchResponse) {
            response.results.map {
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
        } else {
            emptyList()
        }
    }
}
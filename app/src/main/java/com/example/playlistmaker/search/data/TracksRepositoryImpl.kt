package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.search.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(term: String): List<Track> {
        val response = networkClient.searchTracks(TracksSearchRequest(term))
        return if (response.resultCode == 200 && response is TracksSearchResponse) {
            response.results.map {
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
                        year = releaseDate?.substringBefore('-')
                    )
                }
            }
        } else {
            emptyList()
        }
    }
}

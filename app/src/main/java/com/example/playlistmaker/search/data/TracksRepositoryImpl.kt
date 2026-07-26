package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(term: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.searchTracks(TracksSearchRequest(term))
        val tracksResource =
            if (response.resultCode == 200 && response is TracksSearchResponse) {
                val tracks = response.results.map {
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
                Resource.Success(tracks)
            } else {
                Resource.Error("Ошибка сервера")
            }

        emit(tracksResource)
    }
}

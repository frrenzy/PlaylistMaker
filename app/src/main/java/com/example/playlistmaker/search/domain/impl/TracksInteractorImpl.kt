package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.SearchResult
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(term: String, consumer: TracksInteractor.TracksConsumer) {
        var result: SearchResult
        executor.execute {
            try {
                val tracks = repository.searchTracks(term)
                result = when (tracks.isEmpty()) {
                    true -> SearchResult.Empty
                    else -> SearchResult.Success(tracks)
                }
            } catch (e: Exception) {
                result = SearchResult.Error(e.message ?: "unexpected network error")
            }
            consumer.consume(result)
        }
    }
}

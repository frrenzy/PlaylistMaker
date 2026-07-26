package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.utils.network.Response

data class TracksSearchResponse(val results: ArrayList<TrackDto>) : Response()

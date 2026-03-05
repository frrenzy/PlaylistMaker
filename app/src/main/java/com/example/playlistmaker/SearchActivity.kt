package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.track.api.SearchTracksResponse
import com.example.playlistmaker.track.api.TrackApiService
import com.example.playlistmaker.track.model.Track
import com.example.playlistmaker.track.presentation.TrackAdapter
import com.example.playlistmaker.utils.connectBackButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val trackServiceBaseUrl = "https://itunes.apple.com/"
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(trackServiceBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val tracksService = retrofit.create(TrackApiService::class.java)

    private val tracks = ArrayList<Track>()

    private lateinit var trackList: RecyclerView
    private lateinit var searchClearButton: ImageView
    private lateinit var searchField: EditText
    private lateinit var networkErrorBlock: LinearLayout
    private lateinit var notFoundErrorBlock: LinearLayout
    private lateinit var reloadButton: Button

    private val adapter = TrackAdapter()

    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        connectBackButton(R.id.search_back_button)

        searchClearButton = findViewById(R.id.search_clear_button)
        searchField = findViewById(R.id.search_field)
        trackList = findViewById(R.id.track_list)
        networkErrorBlock = findViewById(R.id.network_error_block)
        notFoundErrorBlock = findViewById(R.id.not_found_error_block)
        reloadButton = findViewById(R.id.reload_button)

        adapter.tracks = tracks
        trackList.adapter = adapter

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchClearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        })
        searchField.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    loadTracks()
                    true
                }

                else -> false
            }
        }

        searchClearButton.setOnClickListener {
            searchField.setText("")
            setTrackList()
            hideKeyboard()
        }

        reloadButton.setOnClickListener { loadTracks() }
    }

    private fun loadTracks() {
        val term = searchField.text.toString()
        if (term.isEmpty()) {
            return
        }

        tracksService.search(term).enqueue(object : Callback<SearchTracksResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<SearchTracksResponse?>,
                response: Response<SearchTracksResponse?>
            ) {
                when (response.code()) {
                    200 -> {
                        val results = response.body()?.results
                        if (results?.isEmpty() == true) {
                            showNotFoundErrorMessage()
                        } else {
                            setTrackList(results!!)
                        }
                    }

                    else -> showNetworkErrorMessage()
                }
            }

            override fun onFailure(call: Call<SearchTracksResponse?>, t: Throwable) =
                showNetworkErrorMessage()
        })
    }

    fun showNetworkErrorMessage() {
        hideKeyboard()
        setTrackList()

        networkErrorBlock.visibility = View.VISIBLE
    }

    fun showNotFoundErrorMessage() {
        setTrackList()

        notFoundErrorBlock.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTrackList(newTracks: ArrayList<Track> = ArrayList()) {
        networkErrorBlock.visibility = View.GONE
        notFoundErrorBlock.visibility = View.GONE

        tracks.clear()
        tracks.addAll(newTracks)
        adapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SEARCH_TEXT, searchText)
        outState.putSerializable(TRACKS, tracks)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val search = savedInstanceState.getString(SEARCH_TEXT, SEARCH_DEFAULT)
        searchField.setText(search)

        val trackList = savedInstanceState.getSerializable(TRACKS) as ArrayList<Track>?
        if (trackList != null) {
            setTrackList(trackList)
        } else {
            setTrackList()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_DEFAULT = ""
        const val TRACKS = "TRACKS"
    }
}
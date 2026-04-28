package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.track.api.SearchTracksResponse
import com.example.playlistmaker.track.api.getTracksService
import com.example.playlistmaker.track.history.TrackSearchHistory
import com.example.playlistmaker.track.model.Track
import com.example.playlistmaker.track.presentation.TrackAdapter
import com.example.playlistmaker.utils.connectBackButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private val tracksService = getTracksService()

    private val tracks = ArrayList<Track>()

    private lateinit var trackList: RecyclerView
    private lateinit var searchClearButton: ImageView
    private lateinit var searchField: EditText
    private lateinit var networkErrorBlock: LinearLayout
    private lateinit var notFoundErrorBlock: LinearLayout
    private lateinit var reloadButton: Button
    private lateinit var searchHistoryBlock: LinearLayout
    private lateinit var searchHistoryTrackList: RecyclerView
    private lateinit var searchHistoryClearButton: Button
    private lateinit var progressBar: ProgressBar

    private val searchRunnable = Runnable { searchRequest() }
    private val handler = Handler(Looper.getMainLooper())

    private val adapter = TrackAdapter {
        searchHistory.add(it)
        updateSearchHistoryList()
        openPlayer(it)
    }
    private val historyAdapter = TrackAdapter {
        openPlayer(it)
    }

    private lateinit var searchHistory: TrackSearchHistory

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
        progressBar = findViewById(R.id.progress_circular)

        searchHistoryBlock = findViewById(R.id.search_history)
        searchHistoryTrackList = findViewById(R.id.search_history_track_list)
        searchHistoryClearButton = findViewById(R.id.search_history_clear_button)
        searchHistory = TrackSearchHistory(
            getSharedPreferences(
                PLAYLIST_MAKER_PREFERENCES,
                MODE_PRIVATE
            )
        )

        adapter.tracks = tracks
        trackList.adapter = adapter

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchClearButton.isVisible = clearButtonVisibility(s)
                searchHistoryBlock.isVisible =
                    searchHistoryBlockVisibility(searchField.text, searchField.hasFocus())
                loadTracks()
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        })
        searchField.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    loadTracks(0L)
                    true
                }

                else -> false
            }
        }
        searchField.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryBlock.isVisible =
                searchHistoryBlockVisibility(searchField.text, hasFocus)
        }

        searchClearButton.setOnClickListener {
            searchField.setText("")
            setTrackList()
            hideKeyboard()
        }

        reloadButton.setOnClickListener { loadTracks() }

        searchHistoryTrackList.adapter = historyAdapter
        updateSearchHistoryList()

        searchHistoryClearButton.setOnClickListener {
            searchHistory.clear()
            updateSearchHistoryList()
            searchHistoryBlock.isVisible = false
        }
    }

    private fun searchRequest() {
        val term = searchField.text.toString()
        if (term.isEmpty()) {
            return
        }

        showProgressBar()

        tracksService.search(term).enqueue(object : Callback<SearchTracksResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<SearchTracksResponse?>,
                response: Response<SearchTracksResponse?>
            ) {
                hideProgressBar()
                when (response.code()) {
                    200 -> {
                        val results = response.body()?.results
                        if (results?.isEmpty() == true) {
                            showNotFoundErrorMessage()
                        } else {
                            trackList.isVisible = true
                            setTrackList(results!!)
                        }
                    }

                    else -> showNetworkErrorMessage()
                }
            }

            override fun onFailure(call: Call<SearchTracksResponse?>, t: Throwable) {
                hideProgressBar()
                showNetworkErrorMessage()
            }
        })
    }

    private fun loadTracks(delay: Long = SEARCH_DEBOUNCE_DELAY) {
        handler.removeCallbacks(searchRunnable)
        if (delay == 0L) handler.post(searchRunnable) else handler.postDelayed(
            searchRunnable,
            delay
        )
    }

    fun showNetworkErrorMessage() {
        hideKeyboard()
        setTrackList()

        networkErrorBlock.isVisible = true
    }

    fun showNotFoundErrorMessage() {
        setTrackList()

        notFoundErrorBlock.isVisible = true
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTrackList(newTracks: ArrayList<Track> = ArrayList()) {
        networkErrorBlock.isVisible = false
        notFoundErrorBlock.isVisible = false

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

    private fun clearButtonVisibility(s: CharSequence?) = !s.isNullOrEmpty()

    private fun searchHistoryBlockVisibility(s: CharSequence?, focus: Boolean) =
        (focus && searchHistory.size != 0 && s.isNullOrEmpty())

    private fun updateSearchHistoryList() {
        historyAdapter.tracks = searchHistory.getAll()
        historyAdapter.notifyDataSetChanged()
    }

    private fun showProgressBar() {
        searchHistoryBlock.isVisible = false
        networkErrorBlock.isVisible = false
        notFoundErrorBlock.isVisible = false
        trackList.isVisible = false

        progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        progressBar.isVisible = false
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun openPlayer(track: Track) {
        val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
        intent.putExtra(PLAYER_TRACK_KEY, track)

        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_DEFAULT = ""
        const val TRACKS = "TRACKS"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
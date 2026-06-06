package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.PLAYER_TRACK_KEY
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.history.presentation.HistoryViewModel
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchState
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.utils.connectBackButton

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var historyViewModel: HistoryViewModel
    private val searchViewModel: SearchViewModel by viewModels()

    private val searchAdapter = TrackAdapter {
        historyViewModel.onTrackClick(it)
        openPlayer(it)
    }
    private val historyAdapter = TrackAdapter {
        openPlayer(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        connectBackButton(binding.backButton)

        historyViewModel = ViewModelProvider(
            this, HistoryViewModel.getFactory(this)
        ).get(HistoryViewModel::class.java)

        historyViewModel.observeHistory().observe(this) {
            historyAdapter.tracks = it.tracks
            binding.searchHistoryBlock.isVisible = it.isVisible
            historyAdapter.notifyDataSetChanged()
        }

        searchViewModel.observeSearchState().observe(this) {
            when (it) {
                is SearchState.Default -> setTrackList()
                is SearchState.Error -> showNetworkErrorMessage()
                is SearchState.Empty -> showNotFoundErrorMessage()
                is SearchState.Loading -> showProgressBar()
                is SearchState.Content -> setTrackList(it.tracks)
            }
        }

        binding.apply {
            trackList.adapter = searchAdapter

            searchField.doOnTextChanged { s, _, _, _ ->
                searchClearButton.isVisible = clearButtonVisibility(s)
                historyViewModel.onSearchTextChanged(s)
                searchViewModel.onSearchTextChanged(s)
            }

            searchField.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        searchViewModel.onPressEnter()
                        true
                    }

                    else -> false
                }
            }

            searchField.setOnFocusChangeListener { _, hasFocus ->
                historyViewModel.onSearchFocus(hasFocus)
            }

            searchClearButton.setOnClickListener {
                searchField.setText("")
                searchViewModel.onClear()
                hideKeyboard()
            }

            reloadButton.setOnClickListener {
                searchViewModel.onReload()
            }

            searchHistoryTrackList.adapter = historyAdapter

            searchHistoryClearButton.setOnClickListener {
                historyViewModel.onClear()
            }
        }
    }

    fun showNetworkErrorMessage() {
        hideKeyboard()
        setTrackList()

        binding.networkErrorBlock.isVisible = true
    }

    fun showNotFoundErrorMessage() {
        setTrackList()

        binding.notFoundErrorBlock.isVisible = true
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTrackList(newTracks: List<Track> = emptyList()) {
        hideProgressBar()
        binding.apply {
            networkErrorBlock.isVisible = false
            notFoundErrorBlock.isVisible = false
            trackList.isVisible = true
        }

        searchAdapter.tracks = newTracks
        searchAdapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SEARCH_TEXT, binding.searchField.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val search = savedInstanceState.getString(SEARCH_TEXT, SEARCH_DEFAULT)
        binding.searchField.setText(search)
    }

    private fun clearButtonVisibility(s: CharSequence?) = !s.isNullOrEmpty()

    private fun showProgressBar() {
        binding.apply {
            searchHistoryBlock.isVisible = false
            networkErrorBlock.isVisible = false
            notFoundErrorBlock.isVisible = false
            trackList.isVisible = false

            progressBar.isVisible = true
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
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

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_DEFAULT = ""
    }
}
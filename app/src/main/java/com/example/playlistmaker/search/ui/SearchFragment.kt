package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.history.presentation.HistoryViewModel
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.SearchState
import com.example.playlistmaker.search.presentation.SearchViewModel
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : BindingFragment<FragmentSearchBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchBinding.inflate(inflater, container, false)

    private val historyViewModel: HistoryViewModel by activityViewModel()
    private val searchViewModel: SearchViewModel by activityViewModel()

    private val searchAdapter = TrackAdapter {
        historyViewModel.onTrackClick(it)
        openPlayer(it)
    }
    private val historyAdapter = TrackAdapter {
        openPlayer(it)
    }

    private var searchTerm = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel.observeHistory().observe(viewLifecycleOwner) {
            historyAdapter.tracks = it.tracks
            binding.searchHistoryBlock.isVisible = it.isVisible
            historyAdapter.notifyDataSetChanged()
        }

        searchViewModel.observeSearchState().observe(viewLifecycleOwner) {
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
                searchTerm = s?.toString().orEmpty()
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

            searchField.setText(searchTerm)
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
            activity?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun openPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_DEFAULT = ""
    }
}

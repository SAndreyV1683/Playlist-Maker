package a.sboev.ru.playlistmaker.search.ui

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.audioplayer.ui.AudioPlayerActivity
import a.sboev.ru.playlistmaker.databinding.FragmentSearchBinding
import a.sboev.ru.playlistmaker.library.presentation.ui.BindingFragment
import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.search.presentation.SearchViewModel
import a.sboev.ru.playlistmaker.search.ui.adapters.TrackAdapter
import a.sboev.ru.playlistmaker.search.ui.models.TracksState
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchFragment: BindingFragment<FragmentSearchBinding>() {


    private var editTextStr = ""
    private lateinit var textWatcher: TextWatcher
    private val populateList = mutableListOf<Track>()
    private var searchHistoryList = mutableListOf<Track>()
    private lateinit var onItemClicked: TrackAdapter.OnItemClickListener
    private lateinit var adapter: TrackAdapter

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    val viewModel: SearchViewModel by viewModel {
        parametersOf()
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextStr = savedInstanceState?.getString(SAVED_STRING_KEY, "").orEmpty()
        binding.searchEditText.setText(editTextStr)
        viewModel.observeState().observe(viewLifecycleOwner) { state -> render(state) }

        initListeners()
        viewModel.observeHistoryList().observe(viewLifecycleOwner) { list ->
            if (list != null) {
                searchHistoryList = list
            }
        }
        viewModel.readSearchHistory()
        binding.searchEditText.setText("")
        adapter = TrackAdapter(onItemClicked)
        binding.searchRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.tracks = populateList
        binding.searchRv.adapter = adapter

    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, DEBOUNCE_DELAY)
        }
        return current
    }



    private fun initListeners() {
        onItemClicked = TrackAdapter.OnItemClickListener { track ->
            if (clickDebounce()) {
                if (!binding.searchHistoryTitle.isVisible)
                    viewModel.writeSearchHistory(track)
                val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
                intent.putExtra(AudioPlayerActivity.BUNDLE_KEY, track)
                this.startActivity(intent)
            }
        }

        binding.refreshButton.setOnClickListener {
            binding.progressBar.isVisible = true
            viewModel.searchDebounce(editTextStr)
            binding.errorLayout.visibility = View.GONE
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearTextBtn.visibility = clearButtonVisibility(s)
                editTextStr = s.toString()
                if (binding.searchEditText.hasFocus() && editTextStr.isEmpty() && searchHistoryList.isNotEmpty()) {
                    showSearchHistory()
                } else {
                    viewModel.searchDebounce(editTextStr)
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        }

        binding.searchEditText.addTextChangedListener(textWatcher)
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editTextStr.isBlank() && searchHistoryList.isNotEmpty()) {
                showSearchHistory()
            }
        }

        binding.clearTextBtn.setOnClickListener {
            binding.searchEditText.setText("")
            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            binding.errorLayout.visibility = View.GONE
            populateList.clear()
            adapter.refresh()
        }

        binding.clearSearchHistoryBtn.setOnClickListener {
            searchHistoryList.clear()
            adapter.refresh()
            binding.searchHistoryTitle.visibility = View.GONE
            binding.clearSearchHistoryBtn.visibility = View.GONE
            viewModel.clearSearchHistory()
        }
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Empty -> showEmpty()
            is TracksState.Error -> showError()
            is TracksState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.errorLayout.isVisible = false
        binding.searchRv.isVisible = false
        binding.searchHistoryTitle.isVisible = false
        binding.clearSearchHistoryBtn.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun showEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.errorLayout.isVisible = true
        binding.searchRv.isVisible = false
        binding.searchHistoryTitle.isVisible = false
        binding.clearSearchHistoryBtn.isVisible = false
        binding.errorImage.setImageResource(R.drawable.ic_error)
        binding.errorText.setText(R.string.nothing_found_text)
        binding.refreshButton.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>) {
        binding.progressBar.visibility = View.GONE
        binding.searchRv.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
        populateList.clear()
        populateList.addAll(tracks)
        adapter.tracks = populateList
        adapter.refresh()
        binding.searchHistoryTitle.visibility = View.GONE
        binding.clearSearchHistoryBtn.visibility = View.GONE
    }

    private fun showError() {
        binding.progressBar.visibility = View.GONE
        binding.searchRv.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.searchHistoryTitle.visibility = View.GONE
        binding.clearSearchHistoryBtn.visibility = View.GONE
        binding.errorImage.setImageResource(R.drawable.ic_connection_error)
        binding.errorText.setText(R.string.communication_problems)
        binding.refreshButton.visibility = View.VISIBLE
    }

    private fun showSearchHistory() {
        if (binding.errorLayout.isVisible)
            binding.errorLayout.isVisible = false
        adapter.tracks = searchHistoryList
        adapter.refresh()
        binding.searchRv.visibility = View.VISIBLE
        binding.searchHistoryTitle.visibility = View.VISIBLE
        binding.clearSearchHistoryBtn.visibility = View.VISIBLE
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SAVED_STRING_KEY, editTextStr)
        super.onSaveInstanceState(outState)
    }


    companion object {
        private const val SAVED_STRING_KEY = "SAVED_STRING_KEY"
        private const val DEBOUNCE_DELAY = 1000L
    }
}
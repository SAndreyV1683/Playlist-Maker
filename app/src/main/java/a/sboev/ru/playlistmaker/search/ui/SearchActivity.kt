package a.sboev.ru.playlistmaker.search.ui

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.audioplayer.ui.AudioPlayerActivity
import a.sboev.ru.playlistmaker.audioplayer.ui.AudioPlayerActivity.Companion.BUNDLE_KEY
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
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var editTextStr = ""
    private lateinit var searchEditText: EditText
    private val populateList = mutableListOf<Track>()
    private var searchHistoryList = mutableListOf<Track>()
    private lateinit var onItemClicked: TrackAdapter.OnItemClickListener
    private lateinit var adapter: TrackAdapter
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var refreshButton: Button
    private lateinit var errorLayout: LinearLayout
    private lateinit var searchHistoryTitle: TextView
    private lateinit var clearSearchHistoryBtn: Button
    private lateinit var backButton: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var textWatcher: TextWatcher
    private lateinit var progressBar: ProgressBar
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    val viewModel: SearchViewModel by viewModel {
        parametersOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        viewModel.observeState().observe(this) { state -> render(state) }
        initViews()
        initListeners()
        viewModel.observeHistoryList().observe(this) { list ->
            if (list != null) {
                searchHistoryList = list
            }
        }
        viewModel.readSearchHistory()
        searchEditText.setText("")
        adapter = TrackAdapter(onItemClicked)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.tracks = populateList
        recyclerView.adapter = adapter
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, DEBOUNCE_DELAY)
        }
        return current
    }

    private fun initViews() {
        errorImage = findViewById(R.id.error_image)
        errorText = findViewById(R.id.error_text)
        refreshButton = findViewById(R.id.refresh_button)
        errorLayout = findViewById(R.id.error_layout)
        searchHistoryTitle = findViewById(R.id.search_history_title)
        clearSearchHistoryBtn = findViewById(R.id.clear_search_history_btn)
        searchEditText = findViewById(R.id.search_edit_text)
        recyclerView = findViewById(R.id.search_rv)
        backButton = findViewById(R.id.arrow_back)
        clearButton = findViewById(R.id.clear_text_btn)
        progressBar = findViewById(R.id.progress_bar)
    }

    private fun initListeners() {
        onItemClicked = TrackAdapter.OnItemClickListener { track ->
            if (clickDebounce()) {
                if (!searchHistoryTitle.isVisible)
                    viewModel.writeSearchHistory(track)
                val intent = Intent(this, AudioPlayerActivity::class.java)
                intent.putExtra(BUNDLE_KEY, track)
                this.startActivity(intent)
            }
        }
        backButton.setOnClickListener {
            super.onBackPressed()
        }
        refreshButton.setOnClickListener {
            progressBar.isVisible = true
            viewModel.searchDebounce(editTextStr)
            errorLayout.visibility = View.GONE
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                editTextStr = s.toString()
                if (searchEditText.hasFocus() && editTextStr.isEmpty() && searchHistoryList.isNotEmpty()) {
                    showSearchHistory()
                } else {
                    viewModel.searchDebounce(editTextStr)
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        }

        searchEditText.addTextChangedListener(textWatcher)
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editTextStr.isBlank() && searchHistoryList.isNotEmpty()) {
                showSearchHistory()
            }
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            errorLayout.visibility = View.GONE
            populateList.clear()
            adapter.refresh()
        }

        clearSearchHistoryBtn.setOnClickListener {
            searchHistoryList.clear()
            adapter.refresh()
            searchHistoryTitle.visibility = View.GONE
            clearSearchHistoryBtn.visibility = View.GONE
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
        errorLayout.isVisible = false
        recyclerView.isVisible = false
        searchHistoryTitle.isVisible = false
        clearSearchHistoryBtn.isVisible = false
        progressBar.isVisible = true
    }

    private fun showEmpty() {
        progressBar.visibility = View.GONE
        errorLayout.isVisible = true
        recyclerView.isVisible = false
        searchHistoryTitle.isVisible = false
        clearSearchHistoryBtn.isVisible = false
        errorImage.setImageResource(R.drawable.ic_error)
        errorText.setText(R.string.nothing_found_text)
        refreshButton.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
        populateList.clear()
        populateList.addAll(tracks)
        adapter.tracks = populateList
        adapter.refresh()
        searchHistoryTitle.visibility = View.GONE
        clearSearchHistoryBtn.visibility = View.GONE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        errorLayout.visibility = View.VISIBLE
        searchHistoryTitle.visibility = View.GONE
        clearSearchHistoryBtn.visibility = View.GONE
        errorImage.setImageResource(R.drawable.ic_connection_error)
        errorText.setText(R.string.communication_problems)
        refreshButton.visibility = View.VISIBLE
    }

    private fun showSearchHistory() {
        if (errorLayout.isVisible)
            errorLayout.isVisible = false
        adapter.tracks = searchHistoryList
        adapter.refresh()
        recyclerView.visibility = View.VISIBLE
        searchHistoryTitle.visibility = View.VISIBLE
        clearSearchHistoryBtn.visibility = View.VISIBLE
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        editTextStr = savedInstanceState.getString(SAVED_STRING_KEY, "")
        searchEditText.setText(editTextStr)
        super.onRestoreInstanceState(savedInstanceState)
    }

    companion object {
        private const val SAVED_STRING_KEY = "SAVED_STRING_KEY"
        private const val DEBOUNCE_DELAY = 1000L
    }
}
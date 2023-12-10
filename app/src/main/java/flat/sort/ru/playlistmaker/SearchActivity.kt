package flat.sort.ru.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import flat.sort.ru.playlistmaker.AudioPlayerActivity.Companion.BUNDLE_KEY
import flat.sort.ru.playlistmaker.adapters.TrackAdapter
import flat.sort.ru.playlistmaker.models.Track
import flat.sort.ru.playlistmaker.network.ITunesApi
import flat.sort.ru.playlistmaker.network.ITunesApiService
import flat.sort.ru.playlistmaker.network.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var editTextStr = ""
    private lateinit var searchEditText: EditText
    private val iTunesService: ITunesApiService = ITunesApi.retrofitService
    private val populateList = mutableListOf<Track>()
    private val searchHistoryList = mutableListOf<Track>()
    private val sharedPreferences = App.INSTANCE.sharedPreferences
    private var searchHistory: SearchHistory = SearchHistory(sharedPreferences)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Log.d(TAG, "theme is night ${isNightMode(this)}")
        initViews()
        initListeners()
        searchHistoryList.addAll(searchHistory.read())
        searchEditText.setText("")
        adapter = TrackAdapter(onItemClicked)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.tracks = populateList
        recyclerView.adapter = adapter
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
    }

    private fun initListeners() {
        onItemClicked = TrackAdapter.OnItemClickListener { track ->
            if (searchHistoryList.contains(track)) {
                searchHistoryList.remove(track)
                searchHistoryList.add(0, track)
            } else if (searchHistoryList.size == SEARCH_HISTORY_LIMIT) {
                searchHistoryList.removeAt(searchHistoryList.lastIndex)
                searchHistoryList.add(0, track)
            } else {
                searchHistoryList.add(0, track)
            }
            searchHistory.write(searchHistoryList)

            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(BUNDLE_KEY, track)
            this.startActivity(intent)
        }
        backButton.setOnClickListener {
            super.onBackPressed()
        }
        refreshButton.setOnClickListener {
            makeRequest()
            errorLayout.visibility = View.GONE
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                editTextStr = s.toString()
                if (searchEditText.hasFocus() && editTextStr.isEmpty() && searchHistoryList.isNotEmpty()) {
                    showSearchHistory()
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

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                makeRequest()
            }
            false
        }
        clearSearchHistoryBtn.setOnClickListener {
            searchHistoryList.clear()
            adapter.refresh()
            searchHistoryTitle.visibility = View.GONE
            clearSearchHistoryBtn.visibility = View.GONE
            searchHistory.clear()
        }
    }

    private fun showSearchHistory() {
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

    fun showErrorPlaceHolder(isError: Boolean) {
        recyclerView.visibility = View.GONE
        errorLayout.visibility = View.VISIBLE
        searchHistoryTitle.visibility = View.GONE
        clearSearchHistoryBtn.visibility = View.GONE
        if (isError) {
            if (isNightMode(this))
                errorImage.setImageResource(R.drawable.ic_conn_error_dark_mode)
            else
                errorImage.setImageResource(R.drawable.ic_conn_error_light_mode)
            errorText.setText(R.string.communication_problems)
            refreshButton.visibility = View.VISIBLE
        } else {
            if (isNightMode(this))
                errorImage.setImageResource(R.drawable.ic_error_dark_mode)
            else
                errorImage.setImageResource(R.drawable.ic_error_light_mode)
            errorText.setText(R.string.nothing_found_text)
            refreshButton.visibility = View.GONE
        }
    }

    private fun makeRequest() {
        iTunesService.search(editTextStr).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        if (response.isSuccessful && response.body()?.resultCount!! > 0) {
                            recyclerView.visibility = View.VISIBLE
                            errorLayout.visibility = View.GONE
                            populateList.clear()
                            populateList.addAll(response.body()?.results!!)
                            adapter.tracks = populateList
                            adapter.refresh()
                            searchHistoryTitle.visibility = View.GONE
                            clearSearchHistoryBtn.visibility = View.GONE
                        } else {
                            showErrorPlaceHolder(false)
                        }
                    }

                    else -> {
                        showErrorPlaceHolder(true)
                    }
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                showErrorPlaceHolder(true)
            }
        })
    }

    private fun isNightMode(context: Context): Boolean {
        val nightModeFlags =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    companion object {
        private const val SAVED_STRING_KEY = "SAVED_STRING_KEY"
        private const val SEARCH_HISTORY_LIMIT = 10
        private val TAG = SearchActivity::class.simpleName
    }
}
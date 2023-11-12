package flat.sort.ru.playlistmaker

import android.content.Context
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
import flat.sort.ru.playlistmaker.adapters.TrackAdapter
import flat.sort.ru.playlistmaker.models.Track
import flat.sort.ru.playlistmaker.network.ITunesApi
import flat.sort.ru.playlistmaker.network.ITunesApiService
import flat.sort.ru.playlistmaker.network.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SAVED_STRING_KEY = "SAVED_STRING_KEY"
    }

    private lateinit var recyclerView: RecyclerView
    private var editTextStr = ""
    private lateinit var searchEditText: EditText
    private val iTunesService: ITunesApiService = ITunesApi.retrofitService
    private val populateList = mutableListOf<Track>()
    private val adapter = TrackAdapter()
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var refreshButton: Button
    private lateinit var errorLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Log.d(SearchActivity::class.simpleName, "theme is night ${isNightMode(this)}")
        errorImage = findViewById(R.id.error_image)
        errorText = findViewById(R.id.error_text)
        refreshButton = findViewById(R.id.refresh_button)
        errorLayout = findViewById(R.id.error_layout)
        val backButton = findViewById<ImageView>(R.id.arrow_back)
        backButton.setOnClickListener {
            super.onBackPressed()
        }
        refreshButton.setOnClickListener {
            makeRequest()
            errorLayout.visibility = View.GONE
        }

        val clearButton = findViewById<ImageView>(R.id.clear_text_btn)
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                editTextStr = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        searchEditText = findViewById(R.id.search_edit_text)
        searchEditText.addTextChangedListener(textWatcher)

        searchEditText.setText("")
        clearButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            errorLayout.visibility = View.GONE
            populateList.clear()
            adapter.refresh()
        }

        recyclerView = findViewById(R.id.search_rv)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.tracks = populateList
        recyclerView.adapter = adapter

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                makeRequest()
            }
            false
        }
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
                            adapter.refresh()
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
}
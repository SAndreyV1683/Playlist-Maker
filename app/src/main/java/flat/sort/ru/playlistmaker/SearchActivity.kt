package flat.sort.ru.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SAVED_STRING_KEY = "SAVED_STRING_KEY"
        const val DEF_STRING = ""
    }

    private var editTextStr = ""
    private var searchEditText: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageView>(R.id.arrow_back)
        backButton.setOnClickListener {
            super.onBackPressed()
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
        searchEditText?.addTextChangedListener(textWatcher)
        searchEditText?.setText("")
        clearButton.setOnClickListener {
            searchEditText?.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
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
        editTextStr = savedInstanceState.getString(SAVED_STRING_KEY, DEF_STRING)
        searchEditText?.setText(editTextStr)
        super.onRestoreInstanceState(savedInstanceState)
    }


}
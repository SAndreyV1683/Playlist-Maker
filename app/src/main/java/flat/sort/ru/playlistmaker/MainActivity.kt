package flat.sort.ru.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search_btn)
        val libraryButton = findViewById<Button>(R.id.library_btn)
        val settingButton = findViewById<Button>(R.id.settings_btn)
        val searchBtnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                //Toast.makeText(this@MainActivity, getString(R.string.main_search_btn_toast_text), Toast.LENGTH_SHORT).show()
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        searchButton.setOnClickListener(searchBtnClickListener)
        libraryButton.setOnClickListener {
            //Toast.makeText(this@MainActivity, getString(R.string.main_library_btn_toast_text), Toast.LENGTH_SHORT).show()
            val libraryIntent = Intent(this@MainActivity, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }
        settingButton.setOnClickListener {
            //Toast.makeText(this@MainActivity, getString(R.string.main_settings_btn_toast_text), Toast.LENGTH_SHORT).show()
            val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}
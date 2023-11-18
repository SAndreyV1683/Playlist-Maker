package flat.sort.ru.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = (applicationContext as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        val backButton = findViewById<ImageView>(R.id.arrow_back)
        backButton.setOnClickListener {
            super.onBackPressed()
        }

        val shareButton = findViewById<FrameLayout>(R.id.share_button)
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.extra_text_for_send))
            startActivity(intent)
        }

        val supportButton = findViewById<FrameLayout>(R.id.write_to_support_button)
        supportButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.extra_mail)))
            intent.putExtra(Intent.EXTRA_SUBJECT,  getString(R.string.extra_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.extra_text))
            startActivity(intent)
        }

        val termsButton = findViewById<FrameLayout>(R.id.terms_of_use_button)
        termsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.practicum_offer_uri))
            startActivity(intent)
        }
    }
}
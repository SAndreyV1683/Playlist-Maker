package a.sboev.ru.playlistmaker.settings.ui

import a.sboev.ru.playlistmaker.App
import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.settings.domain.model.EmailData
import a.sboev.ru.playlistmaker.settings.presentation.SettingsViewModel
import android.content.res.Configuration
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModel {
        parametersOf()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        if (isNightMode())
            themeSwitcher.isChecked = true
        else
            themeSwitcher.isChecked = viewModel.getThemeSettings()
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.updateTheme(checked)
        }

        val backButton = findViewById<ImageView>(R.id.arrow_back)
        backButton.setOnClickListener {
            super.onBackPressed()
        }

        val shareButton = findViewById<FrameLayout>(R.id.share_button)
        shareButton.setOnClickListener {
            viewModel.shareApp(getString(R.string.extra_text_for_send))
        }

        val supportButton = findViewById<FrameLayout>(R.id.write_to_support_button)
        supportButton.setOnClickListener {
            val emailData = EmailData(extraMail = arrayOf(getString(R.string.extra_mail)), extraSubject = getString(R.string.extra_subject), extraText =  getString(R.string.extra_text))
            viewModel.openSupport(emailData)
        }

        val termsButton = findViewById<FrameLayout>(R.id.terms_of_use_button)
        termsButton.setOnClickListener {
            viewModel.openTerms(getString(R.string.practicum_offer_uri))
        }
    }
    private fun isNightMode(): Boolean {
        val nightModeFlags =
            App.INSTANCE.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
}
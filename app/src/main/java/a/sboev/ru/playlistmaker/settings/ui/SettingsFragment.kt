package a.sboev.ru.playlistmaker.settings.ui

import a.sboev.ru.playlistmaker.App
import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.databinding.FragmentSettingsBinding
import a.sboev.ru.playlistmaker.library.ui.BindingFragment
import a.sboev.ru.playlistmaker.settings.domain.model.EmailData
import a.sboev.ru.playlistmaker.settings.presentation.SettingsViewModel
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsFragment: BindingFragment<FragmentSettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModel {
        parametersOf()
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isNightMode())
            binding.themeSwitcher.isChecked = true
        else
            binding.themeSwitcher.isChecked = viewModel.getThemeSettings()

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.updateTheme(checked)
        }

        binding.shareButton.setOnClickListener {
            viewModel.shareApp(getString(R.string.extra_text_for_send))
        }


        binding.writeToSupportButton.setOnClickListener {
            val emailData = EmailData(extraMail = arrayOf(getString(R.string.extra_mail)), extraSubject = getString(
                R.string.extra_subject), extraText =  getString(R.string.extra_text))
            viewModel.openSupport(emailData)
        }


        binding.termsOfUseButton.setOnClickListener {
            viewModel.openTerms(getString(R.string.practicum_offer_uri))
        }
    }

    private fun isNightMode(): Boolean {
        val nightModeFlags =
            App.INSTANCE.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
}
package a.sboev.ru.playlistmaker

import a.sboev.ru.playlistmaker.databinding.FragmentMainBinding
import a.sboev.ru.playlistmaker.library.presentation.ui.BindingFragment
import a.sboev.ru.playlistmaker.library.presentation.ui.LibraryFragment
import a.sboev.ru.playlistmaker.search.ui.SearchFragment
import a.sboev.ru.playlistmaker.settings.ui.SettingsFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit

class MainFragment: BindingFragment<FragmentMainBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.container_view, SearchFragment())
                addToBackStack("SearchFragment")
            }
        }
        binding.libraryBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.container_view, LibraryFragment())
                addToBackStack("LibraryFragment")
            }
        }
        binding.settingsBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.container_view, SettingsFragment())
                addToBackStack("SettingsFragment")
            }
        }

    }
}
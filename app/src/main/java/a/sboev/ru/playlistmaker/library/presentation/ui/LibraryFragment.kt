package a.sboev.ru.playlistmaker.library.presentation.ui

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.databinding.FragmentLibraryBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment: BindingFragment<FragmentLibraryBinding>() {

    private lateinit var tabLayoutMediator: TabLayoutMediator
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLibraryBinding {
        return FragmentLibraryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = LibraryViewPagerAdapter(childFragmentManager, lifecycle)
        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_layout_featured_tracks_title)
                1 -> tab.text = getString(R.string.tab_layout_playlists_title)
            }
        }
        tabLayoutMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator.detach()
    }
}
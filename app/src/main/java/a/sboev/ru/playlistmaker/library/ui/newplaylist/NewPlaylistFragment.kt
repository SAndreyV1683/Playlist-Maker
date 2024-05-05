package a.sboev.ru.playlistmaker.library.ui.newplaylist

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.databinding.FragmentNewPlaylistBinding
import a.sboev.ru.playlistmaker.library.ui.BindingFragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible

class NewPlaylistFragment: BindingFragment<FragmentNewPlaylistBinding>() {

    private lateinit var nameTextWatcher: TextWatcher
    private lateinit var descriptionTextWatcher: TextWatcher
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewPlaylistBinding {
        return FragmentNewPlaylistBinding.inflate(inflater,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.nameEdittext.background = AppCompatResources.getDrawable(requireContext(), R.drawable.edittext_select_state)
                    binding.nameEdittextTitle.isVisible = true
                    binding.createButton.isEnabled = true
                } else {
                    binding.nameEdittext.background = AppCompatResources.getDrawable(requireContext(), R.drawable.edittext_default_state)
                    binding.nameEdittextTitle.isVisible = false
                    binding.createButton.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        }

        descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.descriptionEdittext.background = AppCompatResources.getDrawable(requireContext(), R.drawable.edittext_select_state)
                    binding.descriptionEdittextTitle.isVisible = true
                } else {
                    binding.descriptionEdittext.background = AppCompatResources.getDrawable(requireContext(), R.drawable.edittext_default_state)
                    binding.descriptionEdittextTitle.isVisible = false
                }
            }
            override fun afterTextChanged(s: Editable?) { }
        }

        binding.nameEdittext.addTextChangedListener(nameTextWatcher)
        binding.descriptionEdittext.addTextChangedListener(descriptionTextWatcher)
    }

}
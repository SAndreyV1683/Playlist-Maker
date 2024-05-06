package a.sboev.ru.playlistmaker.library.ui.newplaylist

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.databinding.FragmentNewPlaylistBinding
import a.sboev.ru.playlistmaker.library.presentation.viewmodels.NewPlaylistViewModel
import a.sboev.ru.playlistmaker.library.ui.BindingFragment
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment: BindingFragment<FragmentNewPlaylistBinding>() {

    private lateinit var nameTextWatcher: TextWatcher
    private lateinit var descriptionTextWatcher: TextWatcher
    private val playlistViewModel by viewModel<NewPlaylistViewModel>()
    private var playlistName = ""
    private var playlistDescription = ""
    private var playlistImageUri = ""
    private val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            playlistImageUri = uri.toString()
            binding.playlistCover.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.playlistCover.setImageURI(uri)
        }
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewPlaylistBinding {
        return FragmentNewPlaylistBinding.inflate(inflater,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s?.trim().isNullOrEmpty()) {
                    binding.nameEdittext.background = AppCompatResources.getDrawable(requireContext(), R.drawable.edittext_select_state)
                    binding.nameEdittextTitle.isVisible = true
                    binding.createButton.isEnabled = true
                    playlistName = s.toString()
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
                    playlistDescription = s.toString()
                } else {
                    binding.descriptionEdittext.background = AppCompatResources.getDrawable(requireContext(), R.drawable.edittext_default_state)
                    binding.descriptionEdittextTitle.isVisible = false
                }
            }
            override fun afterTextChanged(s: Editable?) { }
        }

        binding.nameEdittext.addTextChangedListener(nameTextWatcher)
        binding.descriptionEdittext.addTextChangedListener(descriptionTextWatcher)
        binding.playlistCover.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.createButton.setOnClickListener {
            playlistViewModel.createNewPlayList(playlistName, playlistDescription, playlistImageUri)
            playlistViewModel.saveImageToPrivateStorage(Uri.parse(playlistImageUri), playlistName)
            Toast.makeText(requireContext(), getString(R.string.playlist_created_message, playlistName), Toast.LENGTH_SHORT).show()
            playlistName = ""
            playlistDescription = ""
            playlistImageUri = ""
        }
        binding.newPlaylistToolbar.setNavigationOnClickListener {
            showDialog()
        }
        val onBackPressedCallback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun showDialog() {
        if (playlistName.isNotEmpty() || playlistDescription.isNotEmpty() || playlistImageUri.isNotEmpty()) {
            MaterialAlertDialogBuilder(requireContext(), R.style.alert_dialog_style)
                .setMessage(getString(R.string.new_playlist_dialog_title))
                .setPositiveButton(getString(R.string.dialog_positive_button_text)) { _, _ ->
                    findNavController().popBackStack()
                }
                .setNegativeButton(getString(R.string.dialog_negative_button_text)) { _, _ ->

                }
                .setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.dialog_background))
                .show()
        } else {
            findNavController().popBackStack()
        }
    }

}
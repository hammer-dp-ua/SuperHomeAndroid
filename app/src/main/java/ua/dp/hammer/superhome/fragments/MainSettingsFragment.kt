package ua.dp.hammer.superhome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ua.dp.hammer.superhome.databinding.FragmentMainSettingsBinding
import ua.dp.hammer.superhome.models.MainSettingsViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

class MainSettingsFragment : Fragment() {
    private val viewModel by activityViewModels<MainSettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentMainSettingsBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.localSettingsRepository =
            LocalSettingsRepository.getInstance(context ?: throw IllegalStateException("Context cannot be null"))
        viewModel.loadSettings()

        binding.setupDevicesButton.setOnClickListener {
            val action = MainSettingsFragmentDirections.actionMainSettingsFragmentToDevicesSetupListFragment()
            this.findNavController().navigate(action)
        }

        binding.saveButton.setOnClickListener {
            viewModel.saveSettings()
        }

        return binding.root
    }
}
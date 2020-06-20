package ua.dp.hammer.superhome.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.dp.hammer.superhome.databinding.FragmentMainSettingsBinding
import ua.dp.hammer.superhome.models.MainSettingsViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

class MainSettingsFragment : Fragment() {
    private val viewModel: MainSettingsViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            val currentContext = context ?: throw IllegalStateException("Context cannot be null")

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                MainSettingsViewModel(LocalSettingsRepository.getInstance(currentContext)) as T
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentMainSettingsBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.loadSettings()

        binding.saveButton.setOnClickListener {
            viewModel.saveSettings()
        }

        return binding.root
    }
}
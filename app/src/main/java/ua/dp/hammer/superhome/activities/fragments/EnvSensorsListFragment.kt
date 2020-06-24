package ua.dp.hammer.superhome.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ua.dp.hammer.superhome.adapters.EnvSensorsListAdapter
import ua.dp.hammer.superhome.data.EnvSensorDisplayedInfo
import ua.dp.hammer.superhome.databinding.FragmentEnvSensorsListBinding
import ua.dp.hammer.superhome.models.EnvSensorViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.utilities.getServerAddress

class EnvSensorsListFragment : Fragment() {
    private val viewModel: EnvSensorViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            val currentContext = context ?: throw IllegalStateException("Context cannot be null")
            val localSettingsRepository = LocalSettingsRepository.getInstance(currentContext)

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return runBlocking {
                    val serverAddress = getServerAddress(context, localSettingsRepository)
                    EnvSensorViewModel(localSettingsRepository, serverAddress) as T
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val currentContext = context ?: throw IllegalStateException("Context cannot be null")
            val serverAddress = getServerAddress(currentContext, LocalSettingsRepository.getInstance(currentContext))
            viewModel.changeServerAddress(serverAddress)
            viewModel.resumeMonitoring()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopMonitoring()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentEnvSensorsListBinding.inflate(inflater, container, false)

        context ?: return binding.root

        val adapter = EnvSensorsListAdapter(this)

        subscribeUi(adapter)
        binding.envSensorsList.adapter = adapter

        binding.root.setOnTouchListener {v, event ->
            true
        }

        return binding.root
    }

    fun stopInfoUpdating() {
        viewModel.stopUpdating = true
    }

    fun resumeInfoUpdating() {
        viewModel.stopUpdating = false
    }

    fun updateVisibility(detachedDisplayedInfo: EnvSensorDisplayedInfo.Detached) {
        viewModel.updateVisibility(detachedDisplayedInfo)
    }

    private fun subscribeUi(adapter: EnvSensorsListAdapter) {
        viewModel.sensors.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
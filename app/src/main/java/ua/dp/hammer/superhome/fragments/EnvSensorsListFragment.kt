package ua.dp.hammer.superhome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.adapters.EnvSensorsListAdapter
import ua.dp.hammer.superhome.data.EnvSensorDisplayedInfo
import ua.dp.hammer.superhome.databinding.FragmentEnvSensorsListBinding
import ua.dp.hammer.superhome.models.EnvSensorViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.utilities.getServerAddress

class EnvSensorsListFragment : Fragment() {
    private val viewModel by activityViewModels<EnvSensorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val localSettingsRepository = LocalSettingsRepository.getInstance(context)

        lifecycleScope.launch {
            val serverAddress = getServerAddress(context)

            if (serverAddress.isNullOrEmpty()) {
                val navController = findNavController()

                navController.navigate(R.id.mainSettingsFragment)
            } else {
                viewModel.localSettingsRepository = localSettingsRepository
                viewModel.setServerAddressAndInit(serverAddress)
                viewModel.startOrResumeMonitoring()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startOrResumeMonitoring()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopMonitoring()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentEnvSensorsListBinding.inflate(inflater, container, false)
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

    fun updateVisibility(detachedDisplayedInfo: EnvSensorDisplayedInfo.Detached, adapter: EnvSensorsListAdapter) {
        viewModel.updateVisibility(detachedDisplayedInfo, adapter)
    }

    private fun subscribeUi(adapter: EnvSensorsListAdapter) {
        viewModel.sensors.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
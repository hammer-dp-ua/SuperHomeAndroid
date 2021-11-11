package ua.dp.hammer.superhome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.adapters.DevicesSetupListAdapter
import ua.dp.hammer.superhome.databinding.FragmentDevicesSetupListBinding
import ua.dp.hammer.superhome.models.DevicesSetupViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.utilities.getServerAddress

class DevicesSetupListFragment : Fragment() {
    val viewModel by activityViewModels<DevicesSetupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.localSettingsRepository = LocalSettingsRepository.getInstance(context)
        viewModel.clearAllDevicesBeforeLoadingNew()

        lifecycleScope.launch {
            val serverAddress = getServerAddress(context)

            if (serverAddress.isNullOrEmpty()) {
                val navController = findNavController()

                navController.navigate(R.id.mainSettingsFragment)
            } else {
                viewModel.setServerAddress(serverAddress)
                viewModel.loadAllDevices()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentDevicesSetupListBinding.inflate(inflater, container, false)
        val adapter = DevicesSetupListAdapter(this, inflater)

        binding.setupDevicesList.adapter = adapter

        viewModel.devices.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.addButton.setOnClickListener {
            // Scroll after a new element is really added
            val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.setupDevicesList.smoothScrollToPosition(0)
                    binding.setupDevicesList.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
            binding.setupDevicesList.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

            viewModel.addNew()
        }

        binding.saveButton.setOnClickListener {
            viewModel.save()
        }

        return binding.root
    }
}
package ua.dp.hammer.superhome.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.adapters.DevicesSetupListAdapter
import ua.dp.hammer.superhome.databinding.FragmentDevicesSetupListBinding
import ua.dp.hammer.superhome.models.DevicesSetupViewModel
import ua.dp.hammer.superhome.utilities.getServerAddress

class DevicesSetupListFragment : Fragment() {
    private val viewModel by activityViewModels<DevicesSetupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        context ?: return binding.root

        val adapter = DevicesSetupListAdapter(this)
        viewModel.devices.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.setupDevicesList.adapter = adapter
        return binding.root
    }
}
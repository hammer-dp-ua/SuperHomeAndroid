package ua.dp.hammer.superhome.fragments

import android.os.Bundle
import android.util.Log
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
import ua.dp.hammer.superhome.utilities.getServerAddress

class DevicesSetupListFragment : Fragment() {
    val viewModel by activityViewModels<DevicesSetupViewModel>()

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
            Log.i(null, "~~~ 'devices' list changed")

            adapter.submitList(it)
        }

        binding.setupDevicesList.adapter = adapter

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
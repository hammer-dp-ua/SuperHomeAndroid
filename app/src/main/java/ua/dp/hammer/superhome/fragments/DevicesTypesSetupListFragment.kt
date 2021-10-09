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
import ua.dp.hammer.superhome.adapters.DevicesTypesSetupListAdapter
import ua.dp.hammer.superhome.databinding.FragmentDevicesTypesSetupListBinding
import ua.dp.hammer.superhome.models.DevicesTypesSetupViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.utilities.getServerAddress

class DevicesTypesSetupListFragment : Fragment() {
    val viewModel by activityViewModels<DevicesTypesSetupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val serverAddress = getServerAddress(context)

            if (serverAddress.isNullOrEmpty()) {
                val navController = findNavController()

                navController.navigate(R.id.mainSettingsFragment)
            } else {
                viewModel.setServerAddress(serverAddress)
                viewModel.loadAllTypes()
            }

            viewModel.localSettingsRepository = LocalSettingsRepository.getInstance(context)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentDevicesTypesSetupListBinding.inflate(inflater, container, false)

        context ?: return binding.root

        val adapter = DevicesTypesSetupListAdapter(this)

        viewModel.types.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.setupDevicesTypesList.adapter = adapter

        binding.addButton.setOnClickListener {
            // Scroll after a new element is really added
            val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.setupDevicesTypesList.smoothScrollToPosition(0)
                    binding.setupDevicesTypesList.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
            binding.setupDevicesTypesList.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

            viewModel.addNew()
        }

        binding.saveButton.setOnClickListener {
            viewModel.save()
        }

        return binding.root
    }
}
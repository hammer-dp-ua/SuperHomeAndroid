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

        viewModel.localSettingsRepository = LocalSettingsRepository.getInstance(context)

        // Without the clearing - on the first fragment opening observable fields work as expected, but on second opening
        // new values of observable fields aren't get updated.
        // List adapter doesn't set LifecycleOwner on the first visible bindings, because content is actually the same.
        viewModel.clearAllTypesBeforeLoadingNew()

        lifecycleScope.launch {
            val serverAddress = getServerAddress(context)

            if (serverAddress.isNullOrEmpty()) {
                val navController = findNavController()

                navController.navigate(R.id.mainSettingsFragment)
            } else {
                viewModel.setServerAddress(serverAddress)
                viewModel.loadAllTypes()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentDevicesTypesSetupListBinding.inflate(inflater, container, false)
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
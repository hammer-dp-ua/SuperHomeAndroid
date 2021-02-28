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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.adapters.AllDevicesTechInfoListAdapter
import ua.dp.hammer.superhome.databinding.FragmentAllDevicesTechInfoListBinding
import ua.dp.hammer.superhome.models.AllDevicesTechInfoViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.utilities.getServerAddress

class AllDevicesTechInfoFragment : Fragment() {
    private val viewModel by activityViewModels<AllDevicesTechInfoViewModel>()

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
        val binding = FragmentAllDevicesTechInfoListBinding.inflate(inflater, container, false)

        context ?: return binding.root

        val adapter = AllDevicesTechInfoListAdapter(this)
        subscribeUi(adapter)
        binding.devicesTechInfoList.adapter = adapter
        lifecycleScope.launch {
            while (true) {
                binding.devicesTechInfoList.smoothScrollToPosition(0)
                delay(30_000)
            }
        }

        return binding.root
    }

    private fun subscribeUi(adapter: AllDevicesTechInfoListAdapter) {
        viewModel.devicesInfo.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
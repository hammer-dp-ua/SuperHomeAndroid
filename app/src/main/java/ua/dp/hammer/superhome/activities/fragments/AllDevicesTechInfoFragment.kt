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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ua.dp.hammer.superhome.adapters.AllDevicesTechInfoListAdapter
import ua.dp.hammer.superhome.databinding.FragmentAllDevicesTechInfoListBinding
import ua.dp.hammer.superhome.models.AllDevicesTechInfoViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.utilities.getServerAddress

class AllDevicesTechInfoFragment : Fragment() {
    private val viewModel: AllDevicesTechInfoViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            val currentContext = context ?: throw IllegalStateException("Context cannot be null")
            val localSettingsRepository = LocalSettingsRepository.getInstance(currentContext)

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return runBlocking {
                    val serverAddress = getServerAddress(context, localSettingsRepository)
                    AllDevicesTechInfoViewModel(localSettingsRepository, serverAddress) as T
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
package ua.dp.hammer.superhome.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.adapters.AllDevicesTechInfoListAdapter
import ua.dp.hammer.superhome.adapters.EnvSensorsListAdapter
import ua.dp.hammer.superhome.databinding.FragmentAllDevicesTechInfoListBinding
import ua.dp.hammer.superhome.models.AllDevicesTechInfoViewModel
import ua.dp.hammer.superhome.models.ManagerViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

class AllDevicesTechInfoFragment : Fragment() {
    private val viewModel: AllDevicesTechInfoViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            val currentContext = context ?: throw IllegalStateException("Context cannot be null")

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                AllDevicesTechInfoViewModel(LocalSettingsRepository.getInstance(currentContext)) as T
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.resumeMonitoring()
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
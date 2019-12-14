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
import androidx.lifecycle.observe
import ua.dp.hammer.superhome.adapters.EnvSensorsListAdapter
import ua.dp.hammer.superhome.data.EnvSensorDisplayedInfo
import ua.dp.hammer.superhome.databinding.FragmentEnvSensorsListBinding
import ua.dp.hammer.superhome.models.EnvSensorViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

class EnvSensorsListFragment : Fragment() {
    private val viewModel: EnvSensorViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            val currentContext = context ?: throw IllegalStateException("Context cannot be null")

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                EnvSensorViewModel(LocalSettingsRepository.getInstance(currentContext)) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Log.i(null, "~~~ " + EnvSensorsListFragment::class.java.simpleName + " state: started")
    }

    override fun onResume() {
        super.onResume()
        Log.i(null, "~~~ " + EnvSensorsListFragment::class.java.simpleName + " state: resumed")
    }

    override fun onPause() {
        super.onPause()
        Log.i(null, "~~~ " + EnvSensorsListFragment::class.java.simpleName + " state: paused")
    }

    override fun onStop() {
        super.onStop()
        Log.i(null, "~~~ " + EnvSensorsListFragment::class.java.simpleName + " state: stopped")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentEnvSensorsListBinding.inflate(inflater, container, false)

        context ?: return binding.root

        val adapter = EnvSensorsListAdapter(this)

        subscribeUi(adapter)
        binding.envSensorsList.adapter = adapter

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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
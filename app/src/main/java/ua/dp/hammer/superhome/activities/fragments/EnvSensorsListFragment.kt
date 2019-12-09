package ua.dp.hammer.superhome.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import ua.dp.hammer.superhome.adapters.EnvSensorsListAdapter
import ua.dp.hammer.superhome.databinding.FragmentEnvSensorsListBinding
import ua.dp.hammer.superhome.models.EnvSensorViewModel

class EnvSensorsListFragment : Fragment() {
    private val viewModel: EnvSensorViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) = EnvSensorViewModel() as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentEnvSensorsListBinding.inflate(inflater, container, false)

        context ?: return binding.root

        val adapter = EnvSensorsListAdapter(this.parentFragmentManager)

        subscribeUi(adapter)
        binding.envSensorsList.adapter = adapter

        return binding.root
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
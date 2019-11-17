package ua.dp.hammer.superhome.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import ua.dp.hammer.superhome.adapters.EnvSensorsListAdapter
import ua.dp.hammer.superhome.databinding.FragmentEnvSensorsListBinding
import ua.dp.hammer.superhome.models.EnvSensorViewModel
import ua.dp.hammer.superhome.models.EnvSensorsListFragmentFactory
import ua.dp.hammer.superhome.repositories.sensors.EnvSensorsRepository
import ua.dp.hammer.superhome.services.EnvSensorsService

class EnvSensorsListFragment : Fragment() {
    private val viewModel: EnvSensorViewModel by viewModels {
        EnvSensorsListFragmentFactory(EnvSensorsRepository.getInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Intent(context, EnvSensorsService::class.java).also { intent ->
            context?.startService(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        val binding = FragmentEnvSensorsListBinding.inflate(inflater, container, false)

        context ?: return binding.root

        val adapter = EnvSensorsListAdapter()

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
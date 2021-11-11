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
import ua.dp.hammer.superhome.adapters.AlarmSourcesSetupListAdapter
import ua.dp.hammer.superhome.databinding.FragmentAlarmSourcesSetupListBinding
import ua.dp.hammer.superhome.models.AlarmSourcesSetupViewModel
import ua.dp.hammer.superhome.utilities.getServerAddress

class AlarmSourcesSetupListFragment : Fragment() {
    val viewModel by activityViewModels<AlarmSourcesSetupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.clearAllAlarmSourcesBeforeLoadingNew()

        lifecycleScope.launch {
            val serverAddress = getServerAddress(context)

            if (serverAddress.isNullOrEmpty()) {
                val navController = findNavController()

                navController.navigate(R.id.mainSettingsFragment)
            } else {
                viewModel.setServerAddress(serverAddress)
                viewModel.loadAllAlarmSources()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentAlarmSourcesSetupListBinding.inflate(inflater, container, false)
        val adapter = AlarmSourcesSetupListAdapter(this)

        viewModel.alarmSources.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.alarmSourcesSetupList.adapter = adapter

        binding.addButton.setOnClickListener {
            // Scroll after a new element is really added
            val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.alarmSourcesSetupList.smoothScrollToPosition(0)
                    binding.alarmSourcesSetupList.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
            binding.alarmSourcesSetupList.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

            viewModel.addNew()
        }

        binding.saveButton.setOnClickListener {
            viewModel.save()
        }

        return binding.root
    }
}
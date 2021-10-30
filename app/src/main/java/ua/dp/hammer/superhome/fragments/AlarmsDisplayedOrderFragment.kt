package ua.dp.hammer.superhome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.adapters.AlarmsDisplayedOrderListAdapter
import ua.dp.hammer.superhome.databinding.FragmentAlarmsDisplayedOrderBinding
import ua.dp.hammer.superhome.models.AlarmsDisplayedOrderViewModel
import ua.dp.hammer.superhome.utilities.getServerAddress

class AlarmsDisplayedOrderFragment : Fragment() {
    val viewModel by activityViewModels<AlarmsDisplayedOrderViewModel>()

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentAlarmsDisplayedOrderBinding.inflate(inflater, container, false)

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

        context ?: return binding.root

        val adapter = AlarmsDisplayedOrderListAdapter(this)

        viewModel.alarmsSources.observe(viewLifecycleOwner) {
            adapter.submitList(it.toList())
        }

        binding.alarmsOrderList.adapter = adapter

        return binding.root
    }
}
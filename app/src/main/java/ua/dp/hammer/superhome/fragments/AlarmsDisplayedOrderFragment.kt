package ua.dp.hammer.superhome.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.alarms_displayed_order_list_item.view.*
import kotlinx.android.synthetic.main.fragment_alarms_displayed_order.*
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.adapters.AlarmsDisplayedOrderListAdapter
import ua.dp.hammer.superhome.data.ImageItem
import ua.dp.hammer.superhome.databinding.FragmentAlarmsDisplayedOrderBinding
import ua.dp.hammer.superhome.models.AlarmsDisplayedOrderViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.utilities.getServerAddress
import ua.dp.hammer.superhome.utilities.notNullContext

class AlarmsDisplayedOrderFragment : Fragment() {
    val viewModel by activityViewModels<AlarmsDisplayedOrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.localSettingsRepository = LocalSettingsRepository.getInstance(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentAlarmsDisplayedOrderBinding.inflate(inflater, container, false)

        viewModel.clearAllAlarmSourcesBeforeLoadingNew()

        lifecycleScope.launch {
            val serverAddress = getServerAddress(context)

            if (serverAddress.isNullOrEmpty()) {
                val navController = findNavController()

                navController.navigate(R.id.mainSettingsFragment)
            } else {
                viewModel.setServerAddress(serverAddress)
                viewModel.loadAllAlarmSources(notNullContext(context))
            }
        }

        val adapter = AlarmsDisplayedOrderListAdapter(this)

        viewModel.alarmsSources.observe(viewLifecycleOwner) {
            adapter.submitList(it.toList())
        }

        binding.alarmsOrderList.adapter = adapter

        return binding.root
    }

    fun setDrawable(alarmItemPosition: Int, imageItem: ImageItem) {
        val button = this.alarms_order_list[alarmItemPosition].displayedIconImageButton


    }
}
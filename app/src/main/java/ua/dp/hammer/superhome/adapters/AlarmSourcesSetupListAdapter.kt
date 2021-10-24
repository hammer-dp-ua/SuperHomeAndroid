package ua.dp.hammer.superhome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.data.setup.AlarmSourceSetupObservable
import ua.dp.hammer.superhome.databinding.AlarmSourceSetupItemBinding
import ua.dp.hammer.superhome.dialogs.DeviceSettingDeleteAlarmSourceConfirmationDialog
import ua.dp.hammer.superhome.fragments.AlarmSourcesSetupListFragment

class AlarmSourcesSetupListAdapter(private val fragment: AlarmSourcesSetupListFragment) :
    ListAdapter<AlarmSourceSetupObservable, RecyclerView.ViewHolder>(AlarmSourcesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AlarmSourceSetupItemBinding.inflate(layoutInflater, parent, false)
        return AlarmSourcesSetupListViewHolder(binding, fragment, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sensor = getItem(position)
        (holder as AlarmSourcesSetupListViewHolder).bind(sensor)
    }
}

private class AlarmSourcesSetupListViewHolder(private val binding: AlarmSourceSetupItemBinding,
                                              private val fragment: AlarmSourcesSetupListFragment,
                                              private val adapter: AlarmSourcesSetupListAdapter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AlarmSourceSetupObservable) {
        binding.lifecycleOwner = fragment

        binding.deleteButton.setOnClickListener {
            if (!item.isNew()) {
                val dialog = DeviceSettingDeleteAlarmSourceConfirmationDialog(fragment.viewModel, item)

                dialog.show(fragment.parentFragmentManager, "confirm_delete_alarm_source_type")
            } else {
                val deletedIndex = fragment.viewModel.delete(item)

                if (deletedIndex >= 0) {
                    adapter.notifyItemRemoved(deletedIndex)
                }
            }
        }

        binding.apply {
            alarmSourceSetup = item
            executePendingBindings()
        }

        if (item.devicesNames.isNotEmpty()) {
            val spinner: Spinner = binding.deviceNamesSpinner
            ArrayAdapter(fragment.context ?: throw IllegalStateException(),
                ua.dp.hammer.superhome.R.layout.spinner_item,
                item.devicesNames).also { spinnerAdapter ->
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = spinnerAdapter
            }
        }
    }
}

private class AlarmSourcesDiffCallback : DiffUtil.ItemCallback<AlarmSourceSetupObservable>() {
    override fun areItemsTheSame(oldItem: AlarmSourceSetupObservable, newItem: AlarmSourceSetupObservable): Boolean {
        return oldItem.aaId == newItem.aaId
    }

    override fun areContentsTheSame(oldItem: AlarmSourceSetupObservable, newItem: AlarmSourceSetupObservable): Boolean {
        return oldItem == newItem
    }
}
package ua.dp.hammer.superhome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.data.setup.DeviceTypeSetupObservable
import ua.dp.hammer.superhome.databinding.DeviceTypeSetupItemBinding
import ua.dp.hammer.superhome.dialogs.DeviceSettingDeleteDeviceTypeConfirmationDialog
import ua.dp.hammer.superhome.fragments.DevicesTypesSetupListFragment

class DevicesTypesSetupListAdapter(private val fragment: DevicesTypesSetupListFragment) :
    ListAdapter<DeviceTypeSetupObservable, RecyclerView.ViewHolder>(DevicesTypesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DeviceTypeSetupItemBinding.inflate(layoutInflater, parent, false)
        return DevicesTypesSetupListViewHolder(binding, fragment, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sensor = getItem(position)
        (holder as DevicesTypesSetupListViewHolder).bind(sensor)
    }
}

private class DevicesTypesSetupListViewHolder(private val binding: DeviceTypeSetupItemBinding,
                                              private val fragment: DevicesTypesSetupListFragment,
                                              private val adapter: DevicesTypesSetupListAdapter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DeviceTypeSetupObservable) {
        binding.lifecycleOwner = fragment.viewLifecycleOwner

        binding.deleteButton.setOnClickListener {
            if (!item.type.value.isNullOrEmpty()) {
                val dialog = DeviceSettingDeleteDeviceTypeConfirmationDialog(fragment.viewModel, item)

                dialog.show(fragment.parentFragmentManager, "confirm_delete_device_type")
            } else {
                val deletedIndex = fragment.viewModel.delete(item)

                if (deletedIndex >= 0) {
                    adapter.notifyItemRemoved(deletedIndex)
                }
            }
        }

        binding.apply {
            deviceTypeSetup = item
            executePendingBindings()
        }
    }
}

private class DevicesTypesDiffCallback : DiffUtil.ItemCallback<DeviceTypeSetupObservable>() {
    override fun areItemsTheSame(oldItem: DeviceTypeSetupObservable, newItem: DeviceTypeSetupObservable): Boolean {
        return oldItem.type.value == newItem.type.value
    }

    override fun areContentsTheSame(oldItem: DeviceTypeSetupObservable, newItem: DeviceTypeSetupObservable): Boolean {
        return oldItem == newItem
    }
}
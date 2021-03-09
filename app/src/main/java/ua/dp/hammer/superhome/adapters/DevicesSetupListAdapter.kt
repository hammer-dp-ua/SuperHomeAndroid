package ua.dp.hammer.superhome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.data.DeviceSetupObservable
import ua.dp.hammer.superhome.databinding.DeviceSetupItemBinding
import ua.dp.hammer.superhome.dialogs.DeviceSettingDeleteConfirmationDialog
import ua.dp.hammer.superhome.fragments.DevicesSetupListFragment

class DevicesSetupListAdapter(private val fragment: DevicesSetupListFragment) :
    ListAdapter<DeviceSetupObservable, RecyclerView.ViewHolder>(SetupDeviceCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DeviceSetupItemBinding.inflate(layoutInflater, parent, false)
        return DevicesSetupListViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sensor = getItem(position)
        (holder as DevicesSetupListViewHolder).bind(sensor)
    }
}

private class DevicesSetupListViewHolder(private val binding: DeviceSetupItemBinding,
                                         private val fragment: DevicesSetupListFragment
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DeviceSetupObservable) {
        binding.lifecycleOwner = fragment

        binding.deleteButton.setOnClickListener {
            val id = item.id.value
            val name = item.name.value

            if (!id.isNullOrEmpty()) {
                val dialog = DeviceSettingDeleteConfirmationDialog(fragment.viewModel, id, name)

                dialog.show(fragment.parentFragmentManager, "confirm_delete_device")
            } else if (!name.isNullOrEmpty()) {
                fragment.viewModel.deleteByName(name)
            }
        }

        binding.apply {
            deviceSetup = item
            executePendingBindings()
        }
    }
}

private class SetupDeviceCallback : DiffUtil.ItemCallback<DeviceSetupObservable>() {
    override fun areItemsTheSame(oldItem: DeviceSetupObservable, newItem: DeviceSetupObservable): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: DeviceSetupObservable, newItem: DeviceSetupObservable): Boolean {
        return oldItem == newItem
    }
}
package ua.dp.hammer.superhome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.activities.fragments.DevicesSetupListFragment
import ua.dp.hammer.superhome.data.DeviceSetupInfo
import ua.dp.hammer.superhome.databinding.DeviceSetupItemBinding

class DevicesSetupListAdapter(private val fragment: DevicesSetupListFragment) : ListAdapter<DeviceSetupInfo, RecyclerView.ViewHolder>(SetupDeviceCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DeviceSetupItemBinding.inflate(layoutInflater, parent, false)
        return DevicesSetupListViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sensor = getItem(position)
        (holder as DevicesSetupListViewHolder).bind(sensor)
    }

    class DevicesSetupListViewHolder(private val binding: DeviceSetupItemBinding,
                                     private val fragment: DevicesSetupListFragment
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DeviceSetupInfo) {
            binding.lifecycleOwner = fragment

            binding.apply {
                deviceSetup = item
                executePendingBindings()
            }
        }
    }
}

private class SetupDeviceCallback : DiffUtil.ItemCallback<DeviceSetupInfo>() {
    override fun areItemsTheSame(oldItem: DeviceSetupInfo, newItem: DeviceSetupInfo): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: DeviceSetupInfo, newItem: DeviceSetupInfo): Boolean {
        return oldItem == newItem
    }
}
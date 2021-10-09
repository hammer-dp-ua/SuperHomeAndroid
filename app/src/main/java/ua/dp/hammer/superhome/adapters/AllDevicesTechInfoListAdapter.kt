package ua.dp.hammer.superhome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.data.DeviceTechInfo
import ua.dp.hammer.superhome.databinding.DeviceTechInfoItemBinding
import ua.dp.hammer.superhome.fragments.AllDevicesTechInfoFragment

class AllDevicesTechInfoListAdapter(private val fragment: AllDevicesTechInfoFragment) :
    ListAdapter<DeviceTechInfo, RecyclerView.ViewHolder>(DeviceTechInfoCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DeviceTechInfoItemBinding.inflate(layoutInflater, parent, false)
        return AllDevicesTechInfoViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sensor = getItem(position)
        (holder as AllDevicesTechInfoViewHolder).bind(sensor)
    }
}

private class AllDevicesTechInfoViewHolder(private val binding: DeviceTechInfoItemBinding,
                                           private val fragment: AllDevicesTechInfoFragment
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DeviceTechInfo) {
        binding.lifecycleOwner = fragment

        binding.apply {
            deviceInfo = item
            executePendingBindings()
        }
    }
}

private class DeviceTechInfoCallback : DiffUtil.ItemCallback<DeviceTechInfo>() {
    override fun areItemsTheSame(oldItem: DeviceTechInfo, newItem: DeviceTechInfo): Boolean {
        return oldItem.deviceName == newItem.deviceName
    }

    override fun areContentsTheSame(oldItem: DeviceTechInfo, newItem: DeviceTechInfo): Boolean {
        return oldItem == newItem
    }
}
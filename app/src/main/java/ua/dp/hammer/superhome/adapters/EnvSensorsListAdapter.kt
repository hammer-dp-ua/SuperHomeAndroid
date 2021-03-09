package ua.dp.hammer.superhome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.databinding.EnvSensorListItemBinding
import ua.dp.hammer.superhome.dialogs.DisplayedEnvSensorInfoDialog
import ua.dp.hammer.superhome.fragments.EnvSensorsListFragment

class EnvSensorsListAdapter(private val fragment: EnvSensorsListFragment) :
    ListAdapter<EnvSensor, RecyclerView.ViewHolder>(EnvSensorCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EnvSensorListItemBinding.inflate(layoutInflater, parent, false)
        return EnvSensorsViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sensor = getItem(position)
        (holder as EnvSensorsViewHolder).bind(sensor)
    }
}

private class EnvSensorsViewHolder(private val binding: EnvSensorListItemBinding,
                                   private val fragment: EnvSensorsListFragment
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: EnvSensor) {
        binding.lifecycleOwner = fragment

        binding.root.setOnLongClickListener {
            val dialog = DisplayedEnvSensorInfoDialog(item, fragment)
            dialog.show(fragment.parentFragmentManager, "sensors_info")
            true
        }

        binding.apply {
            envSensor = item
            executePendingBindings()
        }
    }
}

private class EnvSensorCallback : DiffUtil.ItemCallback<EnvSensor>() {

    override fun areItemsTheSame(oldItem: EnvSensor, newItem: EnvSensor): Boolean {
        return oldItem.deviceName == newItem.deviceName
    }

    override fun areContentsTheSame(oldItem: EnvSensor, newItem: EnvSensor): Boolean {
        return oldItem == newItem
    }
}
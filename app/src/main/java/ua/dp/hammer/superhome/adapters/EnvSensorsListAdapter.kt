package ua.dp.hammer.superhome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.activities.fragments.DisplayedEnvSensorInfoDialog
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.databinding.EnvSensorListItemBinding

class EnvSensorsListAdapter(private val fragmentManager: FragmentManager) : ListAdapter<EnvSensor, RecyclerView.ViewHolder>(EnvSensorCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EnvSensorListItemBinding.inflate(layoutInflater, parent, false)
        return EnvSensorsViewHolder(binding, fragmentManager)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sensor = getItem(position)
        (holder as EnvSensorsViewHolder).bind(sensor)
    }

    class EnvSensorsViewHolder(private val binding: EnvSensorListItemBinding,
                               private val fragmentManager: FragmentManager) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EnvSensor) {
            binding.root.setOnLongClickListener {
                val dialog = DisplayedEnvSensorInfoDialog(item)
                dialog.show(fragmentManager, "sensors_info")
                true
            }

            binding.apply {
                envSensor = item
                executePendingBindings()
            }
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
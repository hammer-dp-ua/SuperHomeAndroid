package ua.dp.hammer.superhome.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.data.EnvSensor

class EnvSensorsListAdapter : ListAdapter<EnvSensor, RecyclerView.ViewHolder>(EnvSensorCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

private class EnvSensorCallback : DiffUtil.ItemCallback<EnvSensor>() {

    override fun areItemsTheSame(oldItem: EnvSensor, newItem: EnvSensor): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: EnvSensor, newItem: EnvSensor): Boolean {
        return oldItem == newItem
    }
}
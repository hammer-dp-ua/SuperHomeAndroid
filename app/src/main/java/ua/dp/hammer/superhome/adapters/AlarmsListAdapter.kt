package ua.dp.hammer.superhome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.data.MotionDetectorInfo
import ua.dp.hammer.superhome.databinding.AlarmsListItemBinding
import ua.dp.hammer.superhome.fragments.AlarmsListFragment

class AlarmsListAdapter(private val fragment: AlarmsListFragment) :
    ListAdapter<MotionDetectorInfo, RecyclerView.ViewHolder>(AlarmSourceInfoCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AlarmsListItemBinding.inflate(layoutInflater, parent, false)
        return AllAlarmSourcesSetupInfoViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as AllAlarmSourcesSetupInfoViewHolder).bind(item)
    }
}

private class AllAlarmSourcesSetupInfoViewHolder(private val binding: AlarmsListItemBinding,
                                                 private val fragment: AlarmsListFragment
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MotionDetectorInfo) {
        binding.lifecycleOwner = fragment

        binding.apply {
            motionDetector = item
            executePendingBindings()
        }
    }
}

private class AlarmSourceInfoCallback : DiffUtil.ItemCallback<MotionDetectorInfo>() {
    override fun areItemsTheSame(oldItem: MotionDetectorInfo, newItem: MotionDetectorInfo): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: MotionDetectorInfo, newItem: MotionDetectorInfo): Boolean {
        return oldItem == newItem
    }
}
package ua.dp.hammer.superhome.adapters

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.data.setup.AlarmDisplayedOrderItemObservable
import ua.dp.hammer.superhome.databinding.AlarmsDisplayedOrderListItemBinding
import ua.dp.hammer.superhome.fragments.AlarmsDisplayedOrderFragment

class AlarmsDisplayedOrderListAdapter(private val fragment: AlarmsDisplayedOrderFragment) :
    ListAdapter<AlarmDisplayedOrderItemObservable, RecyclerView.ViewHolder>(AlarmsDisplayedOrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AlarmsDisplayedOrderListItemBinding.inflate(layoutInflater, parent, false)
        val bindingView = binding.root

        binding.dragItemImageButton.setOnLongClickListener {
            val clipDataItem = ClipData.Item("a tag")
            val dragData = ClipData(
                "a tag",
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                clipDataItem)
            val shadow = ItemDragShadowBuilder(bindingView, fragment.xCoordinatesInsideDragButton)

            bindingView.startDragAndDrop(dragData, shadow, null, 0)
            true
        }

        binding.dragItemImageButton.setOnTouchListener { v, event ->
            fragment.xCoordinatesInsideDragButton = event.x
            false
        }

        return AlarmsDisplayedOrderListViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sensor = getItem(position)
        (holder as AlarmsDisplayedOrderListViewHolder).bind(sensor)
    }
}

private class AlarmsDisplayedOrderListViewHolder(
    private val binding: AlarmsDisplayedOrderListItemBinding,
    private val fragment: AlarmsDisplayedOrderFragment
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AlarmDisplayedOrderItemObservable) {
        binding.lifecycleOwner = fragment

        binding.apply {
            alarmDisplayedOrderItem = item
            executePendingBindings()
        }
    }
}

private class AlarmsDisplayedOrderDiffCallback : DiffUtil.ItemCallback<AlarmDisplayedOrderItemObservable>() {
    override fun areItemsTheSame(oldItem: AlarmDisplayedOrderItemObservable, newItem: AlarmDisplayedOrderItemObservable): Boolean {
        return oldItem.deviceName == newItem.deviceName && oldItem.alarmSource == newItem.alarmSource
    }

    override fun areContentsTheSame(oldItem: AlarmDisplayedOrderItemObservable, newItem: AlarmDisplayedOrderItemObservable): Boolean {
        return oldItem == newItem
    }
}

private class ItemDragShadowBuilder(
    view: View,
    private var xCoordinatesInsideDragButton: Float
) : View.DragShadowBuilder(view) {
    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        size.set(view.width, view.height)
        touch.set(view.width + xCoordinatesInsideDragButton.toInt() - 195, 0)
    }
}
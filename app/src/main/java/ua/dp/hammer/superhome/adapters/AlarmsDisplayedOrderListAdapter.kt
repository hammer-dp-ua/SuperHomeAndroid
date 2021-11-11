package ua.dp.hammer.superhome.adapters

import android.content.ClipData
import android.content.ClipDescription
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_alarms_displayed_order.*
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.data.setup.AlarmDisplayedOrderItemObservable
import ua.dp.hammer.superhome.databinding.AlarmsDisplayedOrderListItemBinding
import ua.dp.hammer.superhome.fragments.AlarmsDisplayedOrderFragment
import ua.dp.hammer.superhome.fragments.AlarmsDisplayedOrderFragmentDirections

class AlarmsDisplayedOrderListAdapter(private val fragment: AlarmsDisplayedOrderFragment) :
        ListAdapter<AlarmDisplayedOrderItemObservable, RecyclerView.ViewHolder>(AlarmsDisplayedOrderDiffCallback()) {

    private val dragListener = DragListener(fragment, this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AlarmsDisplayedOrderListItemBinding.inflate(layoutInflater, parent, false)
        val bindingView = binding.root

        fragment.alarms_order_list.scrollToPosition(0)

        binding.dragItemImageButton.setOnLongClickListener {
            bindingView.setBackgroundResource(R.drawable.draggable_item_border)

            val clipDataItem = ClipData.Item("a tag")
            val dragData = ClipData(
                "a tag",
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                clipDataItem)
            val shadow = View.DragShadowBuilder(it)

            it.startDragAndDrop(dragData, shadow, null, 0)
            true
        }

        bindingView.setOnDragListener(dragListener)

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
        binding.lifecycleOwner = fragment.viewLifecycleOwner

        binding.apply {
            alarmDisplayedOrderItem = item
            executePendingBindings()
        }

        binding.displayedIconImageButton.setOnClickListener {
            val action = AlarmsDisplayedOrderFragmentDirections
                .actionAlarmsDisplayedOrderFragmentToLocalImagesListFragment(item.deviceName, item.alarmSource)
            fragment.findNavController().navigate(action)
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
    view: View
) : View.DragShadowBuilder(view) {
    /*override fun onProvideShadowMetrics(size: Point, touch: Point) {
        size.set(view.width, view.height)
        touch.set(view.width + xCoordinatesInsideDragButton.toInt() - 195, 0)
    }*/
}

private class DragListener(
    private val fragment: AlarmsDisplayedOrderFragment,
    private val adapter: AlarmsDisplayedOrderListAdapter
) : View.OnDragListener {

    private var exitedView: View? = null
    private var exitedIndex: Int = -1
    private var disableDragging = false

    override fun onDrag(view: View?, event: DragEvent?): Boolean {
        when (event?.action) {
            DragEvent.ACTION_DRAG_ENTERED -> {
                val viewIndex = fragment.alarms_order_list.getChildAdapterPosition(view ?: throw IllegalStateException("Can't be null"))

                if (exitedView != null && exitedView != view && !disableDragging) {

                    fragment.viewModel.swap(exitedIndex, viewIndex)
                    adapter.notifyItemMoved(exitedIndex, viewIndex)

                    if (exitedIndex == 0 || viewIndex == 0) {
                        fragment.alarms_order_list.scrollToPosition(0)
                    }
                    disableDragging = true
                }
                return true
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                exitedView = view
                val viewIndex = fragment.alarms_order_list.getChildAdapterPosition(view ?: throw IllegalStateException("Can't be null"))
                exitedIndex = viewIndex
                return true
            }

            DragEvent.ACTION_DROP -> {
                disableDragging = false
                exitedView = null
                exitedIndex = -1
                return true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                view?.background = null
                return true
            }
        }
        return true
    }
}
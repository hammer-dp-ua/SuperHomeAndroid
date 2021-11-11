package ua.dp.hammer.superhome.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.ImageItem
import ua.dp.hammer.superhome.databinding.LocalImageListItemBinding
import ua.dp.hammer.superhome.fragments.LocalImagesListFragment
import ua.dp.hammer.superhome.utilities.getDrawableResourceID
import ua.dp.hammer.superhome.utilities.notNullContext

class LocalImagesListAdapter(private val fragment: LocalImagesListFragment) :
    ListAdapter<ImageItem, RecyclerView.ViewHolder>(LocalImagesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LocalImageListItemBinding.inflate(layoutInflater, parent, false)

        return LocalImagesListViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as LocalImagesListViewHolder).bind(item)
    }
}

private class LocalImagesListViewHolder(
    private val binding: LocalImageListItemBinding,
    private val fragment: LocalImagesListFragment
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ImageItem) {
        //binding.lifecycleOwner = fragment.viewLifecycleOwner

        binding.apply {
            localImage = item

            if (item.imageFilePath != null) {
                val createdDrawable = Drawable.createFromPath(item.imageFilePath.path)
                this.imageView.setImageDrawable(createdDrawable)
            } else if (item.resourceName != null) {
                val resourceId = getDrawableResourceID(notNullContext(fragment.context), item.resourceName)
                this.imageView.setImageResource(resourceId)
            }

            //executePendingBindings()
        }

        binding.imageView.setOnClickListener {
            Log.i(null, "~~~ Image clicked: ${binding.imageView}")
            Log.i(null, "~~~ Parent index: ${fragment.navArgs.deviceName}, ${fragment.navArgs.alarmSource}")

            fragment.lifecycleScope.launch {
                fragment.alarmsDisplayedOrderViewModel
                    .saveAlarmImage(item, fragment.navArgs.deviceName, fragment.navArgs.alarmSource)
                fragment.findNavController().popBackStack()
            }
        }
    }
}

private class LocalImagesDiffCallback : DiffUtil.ItemCallback<ImageItem>() {
    override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem == newItem
    }
}
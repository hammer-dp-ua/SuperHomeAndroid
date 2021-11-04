package ua.dp.hammer.superhome.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.data.ImageItem
import ua.dp.hammer.superhome.databinding.LocalImageListItemBinding
import ua.dp.hammer.superhome.fragments.LocalImagesListFragment

class LocalImagesListAdapter(private val fragment: LocalImagesListFragment) :
    ListAdapter<ImageItem, RecyclerView.ViewHolder>(LocalImagesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LocalImageListItemBinding.inflate(layoutInflater, parent, false)

        binding.imageView.setOnClickListener {
            Log.i(null, "~~~ Image clicked: ${binding.imageView}")
        }

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
        binding.lifecycleOwner = fragment

        binding.apply {
            localImage = item

            if (item.image != null) {
                this.imageView.setImageDrawable(item.image)
            } else if (item.resId > 0) {
                this.imageView.setImageResource(item.resId)
            }
            executePendingBindings()
        }
    }
}

private class LocalImagesDiffCallback : DiffUtil.ItemCallback<ImageItem>() {
    override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem == newItem
    }
}
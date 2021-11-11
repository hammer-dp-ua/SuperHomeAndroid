package ua.dp.hammer.superhome.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.data.setup.DeviceSetupObservable
import ua.dp.hammer.superhome.databinding.DeviceSetupItemBinding
import ua.dp.hammer.superhome.dialogs.DeviceSettingDeleteDeviceConfirmationDialog
import ua.dp.hammer.superhome.fragments.DevicesSetupListFragment

class DevicesSetupListAdapter(
    private val fragment: DevicesSetupListFragment,
    private val layoutInflater: LayoutInflater
) : ListAdapter<DeviceSetupObservable, RecyclerView.ViewHolder>(DevicesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DeviceSetupItemBinding.inflate(layoutInflater, parent, false)
        return DevicesSetupListViewHolder(binding, fragment, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sensor = getItem(position)
        (holder as DevicesSetupListViewHolder).bind(sensor)
    }
}

private class DevicesSetupListViewHolder(private val binding: DeviceSetupItemBinding,
                                         private val fragment: DevicesSetupListFragment,
                                         private val adapter: DevicesSetupListAdapter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DeviceSetupObservable) {
        binding.lifecycleOwner = fragment.viewLifecycleOwner
        binding.deviceSetup = item
        binding.executePendingBindings()

        binding.deleteButton.setOnClickListener {
            val id = item.id.value
            val name = item.name.value

            if (!id.isNullOrEmpty()) {
                val dialog = DeviceSettingDeleteDeviceConfirmationDialog(fragment.viewModel, id, name)

                dialog.show(fragment.parentFragmentManager, "confirm_delete_device")
            } else {
                val deletedIndex = fragment.viewModel.delete(item)
                adapter.notifyItemRemoved(deletedIndex)
            }
        }

        val spinner: Spinner = binding.typeSpinner
        ArrayAdapter(fragment.context ?: throw IllegalStateException(),
            android.R.layout.simple_spinner_item,
            item.displayedTypes).also { spinnerAdapter ->
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = spinnerAdapter
        }
        spinner.setSelection(item.types.indexOf(item.type))
    }
}

private class DevicesDiffCallback : DiffUtil.ItemCallback<DeviceSetupObservable>() {
    override fun areItemsTheSame(oldItem: DeviceSetupObservable, newItem: DeviceSetupObservable): Boolean {
        return oldItem.name.value == newItem.name.value
    }

    override fun areContentsTheSame(oldItem: DeviceSetupObservable, newItem: DeviceSetupObservable): Boolean {
        return oldItem == newItem
    }
}
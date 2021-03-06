package ua.dp.hammer.superhome.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.data.DeviceSetupObservable
import ua.dp.hammer.superhome.databinding.DeviceSetupItemBinding
import ua.dp.hammer.superhome.dialogs.DeviceSettingDeleteConfirmationDialog
import ua.dp.hammer.superhome.fragments.DevicesSetupListFragment

class DevicesSetupListAdapter(private val fragment: DevicesSetupListFragment) :
    ListAdapter<DeviceSetupObservable, RecyclerView.ViewHolder>(SetupDeviceCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
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
        binding.lifecycleOwner = fragment

        binding.deleteButton.setOnClickListener {
            val id = item.id.value
            val name = item.name.value

            if (!id.isNullOrEmpty()) {
                val dialog = DeviceSettingDeleteConfirmationDialog(fragment.viewModel, id, name)

                dialog.show(fragment.parentFragmentManager, "confirm_delete_device")
            } else {
                val deletedIndex = fragment.viewModel.delete(item)
                adapter.notifyItemRemoved(deletedIndex)
            }
        }

        binding.apply {
            deviceSetup = item
            executePendingBindings()
        }

        val spinner: Spinner = binding.typeSpinner
        ArrayAdapter.createFromResource(
            fragment.context ?: throw IllegalStateException(),
            R.array.device_setup_types,
            android.R.layout.simple_spinner_item
        ).also { spinnerAdapter ->
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = spinnerAdapter
        }

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(null, "~~~Position: $position")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d(null, "~~~Not selected")
            }
        }

        val typeValue = item.type.value ?: throw IllegalStateException("Type can't be null")
        spinner.setSelection(typeValue.ordinal)
    }
}

private class SetupDeviceCallback : DiffUtil.ItemCallback<DeviceSetupObservable>() {
    override fun areItemsTheSame(oldItem: DeviceSetupObservable, newItem: DeviceSetupObservable): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: DeviceSetupObservable, newItem: DeviceSetupObservable): Boolean {
        return oldItem == newItem
    }
}
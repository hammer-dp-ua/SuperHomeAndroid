package ua.dp.hammer.superhome.activities.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.databinding.CameraRecordingSettingsDialogBinding
import ua.dp.hammer.superhome.models.CameraRecordingSettingsViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

class CameraRecordingSettingsDialog : DialogFragment() {
    private val viewModel: CameraRecordingSettingsViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            val currentContext = context ?: throw IllegalStateException("Context cannot be null")

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                CameraRecordingSettingsViewModel(LocalSettingsRepository.getInstance(currentContext)) as T
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = CameraRecordingSettingsDialogBinding.inflate(inflater)

            binding.viewModel = viewModel
            binding.lifecycleOwner = this

            binding.timePicker.setIs24HourView(true)

            builder
                .setView(binding.root)
                .setPositiveButton(R.string.OK) { dialog, id ->
                    viewModel.saveSettings(binding.timePicker.hour, binding.timePicker.minute)
                }
                .setNegativeButton(R.string.cancel) { dialog, id ->
                    dialog.cancel()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
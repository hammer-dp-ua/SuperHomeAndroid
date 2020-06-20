package ua.dp.hammer.superhome.activities.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // ThemeOverlay_AppCompat_Dialog makes the dialog width fit its content
            val builder = AlertDialog.Builder(it, R.style.ThemeOverlay_AppCompat_Dialog)
            val inflater = requireActivity().layoutInflater
            val binding = CameraRecordingSettingsDialogBinding.inflate(inflater)

            binding.viewModel = viewModel
            binding.lifecycleOwner = this

            binding.timePicker.setIs24HourView(true)

            val dialog: AlertDialog = builder
                .setView(binding.root)
                .create()

            binding.okButton.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    viewModel.saveSettings(binding.timePicker.hour, binding.timePicker.minute)
                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    viewModel.saveSettings(binding.timePicker.currentHour, binding.timePicker.currentMinute)
                }
                dialog.cancel()
            }
            binding.cancelButton.setOnClickListener {
                dialog.cancel()
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
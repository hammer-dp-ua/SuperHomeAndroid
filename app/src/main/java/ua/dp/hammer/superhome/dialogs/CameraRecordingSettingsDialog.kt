package ua.dp.hammer.superhome.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.databinding.CameraRecordingSettingsDialogBinding
import ua.dp.hammer.superhome.models.CameraRecordingSettingsViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.repositories.web.manager.ManagerWebRepository
import java.util.*

class CameraRecordingSettingsDialog(
    private val managerWebRepository: ManagerWebRepository?
) : DialogFragment() {
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

            val currentDateTime = Calendar.getInstance()
            val currentHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
            val currentMinute = currentDateTime.get(Calendar.MINUTE)

            binding.timePicker.hour = currentHour + 2
            binding.timePicker.minute = currentMinute

            val dialog: AlertDialog = builder
                .setView(binding.root)
                .create()

            binding.okButton.setOnClickListener {
                //viewModel.saveSettings(binding.timePicker.hour, binding.timePicker.minute)
                var deltaMinutes = (binding.timePicker.hour * 60 + binding.timePicker.minute) -
                        (currentHour * 60 + currentMinute)
                if (deltaMinutes < 0) {
                    deltaMinutes += (60 * 24)
                }

                viewModel.viewModelScope.launch {
                    managerWebRepository?.stopVideoRecording(deltaMinutes)
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
package ua.dp.hammer.superhome.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.dp.hammer.superhome.fragments.EnvSensorsListFragment
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.data.EnvSensorDisplayedInfo
import ua.dp.hammer.superhome.databinding.DisplayedEnvSensorInfoDialogBinding
import ua.dp.hammer.superhome.models.EnvSensorDisplayedInfoViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

class DisplayedEnvSensorInfoDialog(private val envSensor: EnvSensor,
                                   private val parentFragment: EnvSensorsListFragment
) : DialogFragment() {
    init {
        parentFragment.stopInfoUpdating()
    }

    private val viewModel: EnvSensorDisplayedInfoViewModel by viewModels {
        val currentContext = context ?: throw IllegalStateException("Context cannot be null")

        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                EnvSensorDisplayedInfoViewModel(LocalSettingsRepository.getInstance(currentContext)) as T
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DisplayedEnvSensorInfoDialogBinding.inflate(inflater)

            binding.viewModel = viewModel
            binding.lifecycleOwner = this
            viewModel.updateLiveData(envSensor)

            val dialog = builder
                .setView(binding.root)
                .create()

            binding.okButton.setOnClickListener {
                val result: EnvSensorDisplayedInfo.Detached = viewModel.onOk {
                    parentFragment.resumeInfoUpdating()
                }

                parentFragment.updateVisibility(result)
                dialog.cancel()
            }
            binding.cancelButton.setOnClickListener {
                parentFragment.resumeInfoUpdating()
                dialog.cancel()
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
package ua.dp.hammer.superhome.activities.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.data.EnvSensorDisplayedInfo
import ua.dp.hammer.superhome.databinding.DisplayedEnvSensorInfoDialogBinding
import ua.dp.hammer.superhome.models.EnvSensorDisplayedInfoViewModel

class DisplayedEnvSensorInfoDialog(private val envSensor: EnvSensor) : DialogFragment() {
    private val viewModel: EnvSensorDisplayedInfoViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) = EnvSensorDisplayedInfoViewModel() as T
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DisplayedEnvSensorInfoDialogBinding.inflate(inflater)

            viewModel.displayedInfo.value = EnvSensorDisplayedInfo(envSensor.deviceName)
            binding.viewModel = viewModel

            builder
                .setView(binding.root)
                .setPositiveButton(R.string.OK, { dialog, id ->
                    Log.i(null, "~~~ OK. \nName: " + viewModel.displayedInfo.value?.name +
                            "\nDisplayed name: " + viewModel.displayedInfo.value?.displayedName?.value)
                })
                .setNegativeButton(R.string.cancel, { dialog, id ->
                    Log.i(null, "~~~ Cancel")
                    dialog.cancel()
                })
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")

        viewModel.displayedInfo.observe(this) {
            Log.i(null, "~~~ state changed. Displayed info: " + it)
        }
        return dialog
    }
}
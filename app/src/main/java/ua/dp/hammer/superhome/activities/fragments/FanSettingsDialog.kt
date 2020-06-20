package ua.dp.hammer.superhome.activities.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.databinding.FanSettingsDialogBinding
import ua.dp.hammer.superhome.models.FanSettingsViewModel
import ua.dp.hammer.superhome.repositories.settings.ServerSettingsRepository

class FanSettingsDialog(val fanName: String) : DialogFragment() {
    private val viewModel: FanSettingsViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                FanSettingsViewModel(ServerSettingsRepository.getInstance()) as T
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // ThemeOverlay_AppCompat_Dialog makes the dialog width fit its content
            val builder = AlertDialog.Builder(it, R.style.ThemeOverlay_AppCompat_Dialog)
            val inflater = requireActivity().layoutInflater
            val binding = FanSettingsDialogBinding.inflate(inflater)

            viewModel.loadSettings(fanName)

            binding.viewModel = viewModel
            binding.lifecycleOwner = this

            val dialog: AlertDialog = builder
                .setView(binding.root)
                .create()

            binding.okButton.setOnClickListener {
                viewModel.saveSettings()
                dialog.cancel()
            }
            binding.cancelButton.setOnClickListener {
                dialog.cancel()
            }

            Log.i(null, "~~~ Atts: " + dialog.window?.attributes)

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
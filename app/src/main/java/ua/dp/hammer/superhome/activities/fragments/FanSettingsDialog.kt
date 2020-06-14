package ua.dp.hammer.superhome.activities.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
            val builder = AlertDialog.Builder(it)
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

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
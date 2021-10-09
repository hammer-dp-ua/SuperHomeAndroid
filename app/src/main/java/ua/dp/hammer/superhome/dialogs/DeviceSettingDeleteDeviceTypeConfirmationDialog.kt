package ua.dp.hammer.superhome.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.data.setup.DeviceTypeSetupObservable
import ua.dp.hammer.superhome.models.DevicesTypesSetupViewModel

class DeviceSettingDeleteDeviceTypeConfirmationDialog(
    private val parentViewModel: DevicesTypesSetupViewModel,
    private val item: DeviceTypeSetupObservable
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            var confirmationMessage = resources.getString(R.string.confirm_delete_dialog_tittle) + " " + item.type.value

            if (!item.displayedType.value.isNullOrBlank()) {
                confirmationMessage += "\n\"" + item.displayedType.value + "\""
            }
            confirmationMessage += "?"

            val dialog: AlertDialog = builder
                .setMessage(confirmationMessage)
                .setPositiveButton(R.string.OK, { dialog, id ->
                    parentViewModel.delete(item)
                })
                .setNegativeButton(R.string.cancel, { dialog, id ->
                    dialog.cancel()
                })
                .create()

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
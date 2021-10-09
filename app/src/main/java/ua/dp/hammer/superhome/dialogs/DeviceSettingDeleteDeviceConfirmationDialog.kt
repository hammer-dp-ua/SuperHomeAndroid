package ua.dp.hammer.superhome.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.models.DevicesSetupViewModel

class DeviceSettingDeleteDeviceConfirmationDialog(
    private val parentViewModel: DevicesSetupViewModel,
    private val deviceId: String,
    private val deviceName: String?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val dialog: AlertDialog = builder
                .setMessage(resources.getString(R.string.confirm_delete_dialog_tittle) + "\n\"" + deviceName + "\"?")
                .setPositiveButton(R.string.OK, { dialog, id ->
                    parentViewModel.deleteById(deviceId)
                })
                .setNegativeButton(R.string.cancel, { dialog, id ->
                    dialog.cancel()
                })
                .create()

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
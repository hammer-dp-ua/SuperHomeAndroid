package ua.dp.hammer.superhome.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.data.setup.AlarmSourceSetupObservable
import ua.dp.hammer.superhome.models.AlarmSourcesSetupViewModel

class DeviceSettingDeleteAlarmSourceConfirmationDialog(
    private val parentViewModel: AlarmSourcesSetupViewModel,
    private val item: AlarmSourceSetupObservable
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val confirmationMessage = resources.getString(R.string.confirm_delete_dialog_tittle) + " " +
                    item.alarmSource.value + "\nof \"" + item.deviceName.value + "\"?"

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
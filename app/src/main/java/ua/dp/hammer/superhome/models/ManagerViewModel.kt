package ua.dp.hammer.superhome.models

import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.ViewModel

class ManagerViewModel : ViewModel() {

    fun onProjectorsButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val prevSelectedState = button.isSelected

        button.isSelected = !button.isSelected
    }

    fun onProjectorsLongButtonClick(button: ImageButton) {

    }

    fun onCameraRecordingButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val prevSelectedState = button.isSelected

        button.isSelected = !button.isSelected

    }

    fun onCameraRecordingLongButtonClick(button: ImageButton) {

    }
}
package ua.dp.hammer.superhome.models

import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.AlarmsState
import ua.dp.hammer.superhome.data.AllStates
import ua.dp.hammer.superhome.data.ProjectorState
import ua.dp.hammer.superhome.repositories.manager.ManagerRepository

class ManagerViewModel : ViewModel() {
    val managerRepository: ManagerRepository = ManagerRepository.getInstance()

    val disabledCameraMinutesRemaining: MutableLiveData<String> = MutableLiveData()
    val cameraMinutesRemainingStatusVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)

    init {
        viewModelScope.launch {
            var success = false

            while (!success) {
                try {
                    val response: AllStates = managerRepository.getCurrentStates()

                    applyAllState(response)
                    success = true
                } catch (e: Throwable) {
                    delay(10_000)
                }
            }

            var oftenAmount = 0

            while (isActive) {
                try {
                    val requestStartTime = System.currentTimeMillis()
                    val response: AllStates = managerRepository.getCurrentStatesDeferred()
                    val requestEndTime = System.currentTimeMillis()

                    applyAllState(response)

                    if ((requestEndTime - requestStartTime) < 1_000) {
                        // Too often
                        oftenAmount++
                    } else {
                        oftenAmount = 0;
                    }

                    if (oftenAmount >= 3) {
                        delay(10_000)
                        oftenAmount = 0;
                    }
                } catch (e: Throwable) {
                    delay(10_000)
                }
            }
        }
    }

    private fun applyAllState(allSatesResponse: AllStates) {
        val stopCameraRecordingTimeout: Int = allSatesResponse.alarmsState.minutesRemaining

        if (stopCameraRecordingTimeout > 0) {
            disabledCameraMinutesRemaining.value = stopCameraRecordingTimeout.toString()
            cameraMinutesRemainingStatusVisibility.value = View.VISIBLE
        } else {
            cameraMinutesRemainingStatusVisibility.value = View.GONE
        }
    }

    fun onProjectorsButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val prevSelectedState = button.isSelected

        button.isSelected = !button.isSelected

        viewModelScope.launch {
            val stateParam = when (button.isSelected) {
                true -> "turnOn"
                else -> "turnOff"
            }

            var response: ProjectorState? = null

            try {
                response = managerRepository.switchProjectors(stateParam)
            } catch (e: Throwable) {
                Log.d(null, "~~~ Error on switching projectors", e)
            }

            if (response == null || response.state != stateParam) {
                button.isSelected = prevSelectedState
            }
        }
    }

    fun onProjectorsLongButtonClick(button: ImageButton) {

    }

    fun onCameraRecordingButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val prevSelectedState = button.isSelected

        // Selected means start ignoring alarms and stop video recording
        button.isSelected = !button.isSelected
        val ignoring = button.isSelected

        viewModelScope.launch {
            var response: AlarmsState? = null

            try {
                response = managerRepository.stopVideoRecording(1)
            } catch (e: Throwable) {
                Log.d(null, "~~~ Error on changing alarms ignoring state", e)
            }

            if (response == null || response.ignoring != ignoring) {
                button.isSelected = prevSelectedState
            }
        }
    }

    fun onCameraRecordingLongButtonClick(button: ImageButton) {

    }

    fun onFanButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val prevSelectedState = button.isSelected

        button.isSelected = !button.isSelected
    }

    fun onFanLongButtonClick(button: ImageButton) {

    }
}
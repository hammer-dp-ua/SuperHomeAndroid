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
import ua.dp.hammer.superhome.data.*
import ua.dp.hammer.superhome.db.entities.CameraSettingsEntity
import ua.dp.hammer.superhome.repositories.manager.ManagerRepository
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import java.util.*

class ManagerViewModel(private val localSettingsRepository: LocalSettingsRepository) : ViewModel() {
    val managerRepository: ManagerRepository = ManagerRepository.getInstance()

    val projectorsButtonSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    val cameraButtonSelected: MutableLiveData<Boolean> = MutableLiveData(true)
    val fanButtonSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    val fanButtonEnabled: MutableLiveData<Boolean> = MutableLiveData(true)
    val fanWorkingMinutesRemaining: MutableLiveData<String> = MutableLiveData()
    val fanWorkingMinutesRemainingStatusVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val disabledCameraMinutesRemaining: MutableLiveData<String> = MutableLiveData()
    val cameraMinutesRemainingStatusVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val roomShutterButtonSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    val kitchen1ShutterButtonSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    val kitchen2ShutterButtonSelected: MutableLiveData<Boolean> = MutableLiveData(false)

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

        updateFanState(allSatesResponse.fanState)
        projectorsButtonSelected.value = allSatesResponse.projectorState.turnedOn
        cameraButtonSelected.value = allSatesResponse.alarmsState.ignoring
        for (shutterState in allSatesResponse.shuttersState) {
            if (shutterState.name == "Room shutter") {
                roomShutterButtonSelected.value = !shutterState.opened
            } else if (shutterState.name == "Kitchen shutter 1") {
                kitchen1ShutterButtonSelected.value = !shutterState.opened
            } else if (shutterState.name == "Kitchen shutter 2") {
                kitchen2ShutterButtonSelected.value = !shutterState.opened
            }
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
        val stopRecording = button.isSelected

        viewModelScope.launch {
            var response: AlarmsState? = null
            val currentSettings: CameraSettingsEntity? = localSettingsRepository.getCurrentCameraSettings()

            val timeout = fun(): Int {
                if (stopRecording) {
                    if (currentSettings == null) {
                        return 60 // Let it be a default value
                    }

                    val currentDateTime = Calendar.getInstance()
                    val currentHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
                    val currentMinute = currentDateTime.get(Calendar.MINUTE)
                    var deltaHours = currentSettings.resumeRecordingHour - currentHour
                    val deltaMinutes = currentSettings.resumeRecordingMinute - currentMinute

                    if (deltaHours < 0) {
                        deltaHours += 24
                    }
                    return deltaHours * 60 + deltaMinutes
                } else {
                    return -1
                }
            }

            try {
                response = managerRepository.stopVideoRecording(timeout())
            } catch (e: Throwable) {
                Log.d(null, "~~~ Error on changing alarms ignoring state", e)
            }

            if (response == null || response.ignoring != stopRecording) {
                button.isSelected = prevSelectedState
            }
        }
    }

    fun onFanButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val prevSelectedState = button.isSelected

        button.isSelected = !button.isSelected
        fanButtonEnabled.value = false
        fanButtonSelected.value = true
        val turnOn = button.isSelected

        viewModelScope.launch {
            var response: FanState? = null

            try {
                if (turnOn) {
                    response = managerRepository.turnOnBathroomFan()
                }
            } catch (e: Throwable) {
                Log.d(null, "~~~ Error on changing fan state", e)
            }

            if (response == null) {
                button.isSelected = prevSelectedState
            } else {
                updateFanState(response)
            }
        }
    }

    private fun updateFanState(response: FanState) {
        fanWorkingMinutesRemaining.value = response.minutesRemaining.toString()

        if (response.minutesRemaining > 0 && response.turnedOn) {
            fanWorkingMinutesRemainingStatusVisibility.value = View.VISIBLE
        } else {
            fanWorkingMinutesRemainingStatusVisibility.value = View.GONE
        }

        if (fanButtonSelected.value != response.turnedOn) {
            // Fan state changed
            fanButtonEnabled.value = true
        }
        fanButtonSelected.value = response.turnedOn
    }

    fun onFanLongButtonClick(button: ImageButton) {

    }

    fun onKitchenShutter1ButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val prevSelectedState = button.isSelected

        button.isSelected = !button.isSelected
        val close = button.isSelected

        viewModelScope.launch {
            var response: ShutterState? = null
            val shutterName = "Kitchen shutter 1"

            try {
                response = managerRepository.changeShutterStateAsync(shutterName, !close)
            } catch (e: Throwable) {
                Log.d(null, "~~~ Error on changing '$shutterName' shutter state", e)
            }

            if (response == null) {
                kitchen1ShutterButtonSelected.value = prevSelectedState
            } else {
                kitchen1ShutterButtonSelected.value = close
            }
        }
    }

    fun onKitchenShutter1LongButtonClick(view: View) {

    }

    fun onKitchenShutter2ButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val prevSelectedState = button.isSelected

        button.isSelected = !button.isSelected
        val close = button.isSelected

        viewModelScope.launch {
            var response: ShutterState? = null
            val shutterName = "Kitchen shutter 2"

            try {
                response = managerRepository.changeShutterStateAsync(shutterName, !close)
            } catch (e: Throwable) {
                Log.d(null, "~~~ Error on changing '$shutterName' shutter state", e)
            }

            if (response == null) {
                kitchen2ShutterButtonSelected.value = prevSelectedState
            } else {
                kitchen2ShutterButtonSelected.value = close
            }
        }
    }

    fun onKitchenShutter2LongButtonClick(view: View) {

    }

    fun onRoomShutterButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val prevSelectedState = button.isSelected

        button.isSelected = !button.isSelected
        val close = button.isSelected

        viewModelScope.launch {
            var response: ShutterState? = null
            val shutterName = "Room shutter"

            try {
                response = managerRepository.changeShutterStateAsync(shutterName, !close)
            } catch (e: Throwable) {
                Log.d(null, "~~~ Error on changing '$shutterName' shutter state", e)
            }

            if (response == null) {
                roomShutterButtonSelected.value = prevSelectedState
            } else {
                roomShutterButtonSelected.value = close
            }
        }
    }

    fun onRoomShutterLongButtonClick(view: View) {

    }
}
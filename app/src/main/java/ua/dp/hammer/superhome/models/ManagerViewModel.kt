package ua.dp.hammer.superhome.models

import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
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

    val projectorsButtonState: MutableLiveData<ProjectorState> = MutableLiveData()
    val cameraButtonSelected: MutableLiveData<Boolean> = MutableLiveData(true)
    val fanButtonSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    val fanButtonEnabled: MutableLiveData<Boolean> = MutableLiveData(true)
    val fanWorkingMinutesRemaining: MutableLiveData<String> = MutableLiveData()
    val fanWorkingMinutesRemainingStatusVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val disabledCameraMinutesRemaining: MutableLiveData<String> = MutableLiveData()
    val cameraMinutesRemainingStatusVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val roomShutterButtonState: MutableLiveData<ShutterState> = MutableLiveData()
    val kitchen1ShutterButtonState: MutableLiveData<ShutterState> = MutableLiveData()
    val kitchen2ShutterButtonState: MutableLiveData<ShutterState> = MutableLiveData()

    private var statesJob: Job

    init {
        statesJob = startMonitoring()
    }

    fun stopMonitoring() {
        statesJob.cancel()
    }

    fun resumeMonitoring() {
        if (statesJob.isCancelled) {
            statesJob = startMonitoring()
        }
    }

    private fun startMonitoring(): Job {
        return viewModelScope.launch {
            var success = false

            while (!success) {
                try {
                    val response: AllStates = managerRepository.getCurrentStates()

                    applyAllStates(response)
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

                    applyAllStates(response)

                    if ((requestEndTime - requestStartTime) < 1_000) {
                        // Too often
                        oftenAmount++
                    } else {
                        oftenAmount = 0
                    }

                    if (oftenAmount >= 3) {
                        delay(10_000)
                        oftenAmount = 0;
                    }
                } catch (e: Throwable) {
                    delay(10_000)
                }
            }

            Log.i(null, "~~~ Job has been cancelled")
        }
    }

    private fun applyAllStates(allSatesResponse: AllStates) {
        val stopCameraRecordingTimeout: Int = allSatesResponse.alarmsState.minutesRemaining

        if (stopCameraRecordingTimeout > 0) {
            disabledCameraMinutesRemaining.value = stopCameraRecordingTimeout.toString()
            cameraMinutesRemainingStatusVisibility.value = View.VISIBLE
        } else {
            cameraMinutesRemainingStatusVisibility.value = View.GONE
        }

        updateFanState(allSatesResponse.fanState)
        updateProjectorState(allSatesResponse.projectorsState)

        cameraButtonSelected.value = allSatesResponse.alarmsState.ignoring

        if (allSatesResponse.shuttersState == null || allSatesResponse.shuttersState.isEmpty()) {
            setShutterNotAvailable(roomShutterButtonState)
            setShutterNotAvailable(kitchen1ShutterButtonState)
            setShutterNotAvailable(kitchen2ShutterButtonState)
        } else {
            var roomShutterProcessed = false
            var kitchenShutterProcessed = false

            for (shutterState in allSatesResponse.shuttersState) {
                if (shutterState.name == "Room shutter") {
                    roomShutterButtonState.value = shutterState
                    roomShutterProcessed = true
                } else if (shutterState.name == "Kitchen shutter") {
                    kitchenShutterProcessed = true
                    if (shutterState.shutterNo == 1) {
                        kitchen1ShutterButtonState.value = shutterState
                    } else if (shutterState.shutterNo == 2) {
                        kitchen2ShutterButtonState.value = shutterState
                    }
                }
            }

            if (!roomShutterProcessed) {
                setShutterNotAvailable(roomShutterButtonState)
            }
            if (!kitchenShutterProcessed) {
                setShutterNotAvailable(kitchen1ShutterButtonState)
                setShutterNotAvailable(kitchen2ShutterButtonState)
            }
        }
    }

    fun onProjectorsButtonClick(view: View) {
        val projectorState = projectorsButtonState.value
        if (projectorState == null || projectorState.notAvailable) {
            return
        }

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

            if (response == null) {
                button.isSelected = prevSelectedState

                val state = ProjectorState()
                state.turnedOn = button.isSelected
                projectorsButtonState.value = state
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

    private fun updateProjectorState(receivedProjectorsState: List<ProjectorState>?) {
        var currentProjectorButtonState = projectorsButtonState.value
        if (receivedProjectorsState == null || receivedProjectorsState.isEmpty()) {
            currentProjectorButtonState = ProjectorState()
        } else {
            if (currentProjectorButtonState == null) {
                currentProjectorButtonState = ProjectorState()
            }

            var turnOn = false
            var notAvailable = true
            for (receivedProjectorState in receivedProjectorsState) {
                // At least 1 is turned on
                turnOn = turnOn || receivedProjectorState.turnedOn
                // At least 1 is available
                notAvailable = notAvailable && receivedProjectorState.notAvailable
            }
            currentProjectorButtonState.turnedOn = turnOn
            currentProjectorButtonState.notAvailable = notAvailable
        }
        projectorsButtonState.value = currentProjectorButtonState
    }

    fun onFanLongButtonClick(button: ImageButton) {

    }

    fun onKitchenShutter1ButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val state: ShutterState? = kitchen1ShutterButtonState.value
        sendShutterRequest(state)
    }

    fun onKitchenShutter1LongButtonClick(view: View) {

    }

    fun onKitchenShutter2ButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val state: ShutterState? = kitchen2ShutterButtonState.value
        sendShutterRequest(state)
    }

    fun onKitchenShutter2LongButtonClick(view: View) {

    }

    fun onRoomShutterButtonClick(view: View) {
        val button: ImageButton = view as ImageButton
        val state: ShutterState? = roomShutterButtonState.value
        sendShutterRequest(state)
    }

    fun onRoomShutterLongButtonClick(view: View) {

    }

    private fun sendShutterRequest(state: ShutterState?) {
        if (state == null || state.notAvailable) {
            return
        }

        val open = when (state.state) {
            ShutterStates.SHUTTER_CLOSED -> true
            ShutterStates.SHUTTER_OPENED -> false
            else -> return
        }

        viewModelScope.launch {
            var response: ShutterState? = null

            try {
                response = managerRepository.doShutter(state.name, state.shutterNo, open)
            } catch (e: Throwable) {
                Log.d(null, "~~~ Error on changing '$state' shutter state", e)
            }

            if (response == null) {
                setShutterNotAvailable(roomShutterButtonState)
                setShutterNotAvailable(kitchen1ShutterButtonState)
                setShutterNotAvailable(kitchen2ShutterButtonState)
            }
        }
    }

    private fun setShutterNotAvailable(shutterState: MutableLiveData<ShutterState>) {
        var currentState = shutterState.value

        if (currentState == null) {
            currentState = ShutterState()
        } else {
            currentState.notAvailable = true
        }

        shutterState.value = currentState
    }
}
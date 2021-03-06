package ua.dp.hammer.superhome.models

import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.*
import ua.dp.hammer.superhome.repositories.web.manager.ManagerWebRepository

class ManagerViewModel : AbstractMonitoringViewModel() {
    val projectorsButtonState: MutableLiveData<ProjectorState> = MutableLiveData()
    val cameraButtonSelected: MutableLiveData<Boolean> = MutableLiveData(true)
    val fanWorkingMinutesRemaining: MutableLiveData<String> = MutableLiveData()
    val fanWorkingMinutesRemainingStatusVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val disabledCameraMinutesRemaining: MutableLiveData<String> = MutableLiveData()
    val cameraMinutesRemainingStatusVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val roomShutterButtonState: MutableLiveData<ShutterState> = MutableLiveData()
    val kitchen1ShutterButtonState: MutableLiveData<ShutterState> = MutableLiveData()
    val kitchen2ShutterButtonState: MutableLiveData<ShutterState> = MutableLiveData()
    val fanButtonState: MutableLiveData<FanState> = MutableLiveData()

    lateinit var managerWebRepository: ManagerWebRepository
        private set

    override fun setServerAddressAndInit(serverAddress: String) {
        if (notInitialized || managerWebRepository.address != serverAddress) {
            managerWebRepository = ManagerWebRepository(serverAddress)
            init()
        }
    }

    override fun startMonitoring(): Job {
        return viewModelScope.launch {
            var success = false

            while (!success) {
                var response: AllStates? = null

                try {
                    response = managerWebRepository.getCurrentStates()
                } catch (e: Throwable) {
                    Log.d(null, "", e)
                }

                if (response == null) {
                    delay(10_000)
                    continue
                }

                applyAllStates(response)
                success = true
            }

            var oftenAmount = 0

            while (isActive) {
                val requestStartTime = System.currentTimeMillis()
                var response: AllStates? = null

                try {
                    response = managerWebRepository.getCurrentStatesDeferred()
                } catch (e: Throwable) {
                    Log.d(null, "", e)
                }

                val requestEndTime = System.currentTimeMillis()
                if ((requestEndTime - requestStartTime) < 1_000) {
                    // Too often
                    oftenAmount++
                } else {
                    oftenAmount = 0
                }

                if (oftenAmount >= 3) {
                    delay(10_000)
                    oftenAmount = 0
                    continue
                }
                if (response == null) {
                    continue
                }

                applyAllStates(response)
            }
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
                if (shutterState.deviceName == "Room shutter") {
                    roomShutterButtonState.value = shutterState
                    roomShutterProcessed = true
                } else if (shutterState.deviceName == "Kitchen shutter") {
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
        if (projectorState?.notAvailable == null || projectorState.notAvailable == true) {
            return
        }

        viewModelScope.launch {
            val stateParam = when (projectorState.turnedOn) {
                true -> "turnOff"
                else -> "turnOn"
            }

            var response: ProjectorState? = null

            try {
                response = managerWebRepository.switchProjectors(stateParam)
            } catch (e: Throwable) {
            }

            if (response == null) {
                val state = ProjectorState()
                state.turnedOn = projectorState.turnedOn
                projectorsButtonState.value = state
            }
        }
    }

    fun onProjectorsLongButtonClick(button: ImageButton) {

    }

    fun onCameraRecordingButtonClick(view: View) {
        val button: ImageButton = view as ImageButton

        // Selected means start ignoring alarms and stop video recording
        button.isSelected = !button.isSelected
        val stopRecording = button.isSelected

        viewModelScope.launch {
            val timeout = fun(): Int {
                return if (stopRecording) {
                    60
                } else {
                    -1
                }
            }

            managerWebRepository.stopVideoRecording(timeout())
        }
    }

    fun onFanButtonClick(view: View) {
        val currentFanState = fanButtonState.value

        if (currentFanState == null || currentFanState.turnedOn == true ||
            currentFanState.notAvailable == true) {
            return
        }

        viewModelScope.launch {
            var response: FanState? = null

            try {
                response = managerWebRepository.turnOnBathroomFan()
                updateFanState(response)
            } catch (e: Throwable) {
            }

            if (response == null) {
                currentFanState.notAvailable = true
                fanButtonState.value = currentFanState
            }
        }
    }

    private fun updateFanState(response: FanState?) {
        if (response == null) {
            return
        }

        var currentFanState = fanButtonState.value

        if (currentFanState == null) {
            currentFanState = FanState()
        }

        fanWorkingMinutesRemaining.value = response.minutesRemaining.toString()

        if (response.minutesRemaining != null && response.minutesRemaining > 0 &&
            response.turnedOn == true) {
            fanWorkingMinutesRemainingStatusVisibility.value = View.VISIBLE
        } else {
            fanWorkingMinutesRemainingStatusVisibility.value = View.GONE
        }

        currentFanState.turnedOn = response.turnedOn
        currentFanState.notAvailable = response.notAvailable
        currentFanState.deviceName = response.deviceName

        fanButtonState.value = currentFanState
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
                // At least 1 is available
                notAvailable = notAvailable &&
                        (receivedProjectorState.notAvailable == null || receivedProjectorState.notAvailable == true)
                // At least 1 available is turned on
                turnOn = turnOn || (receivedProjectorState.turnedOn && receivedProjectorState.notAvailable == false)
            }
            currentProjectorButtonState.turnedOn = turnOn
            currentProjectorButtonState.notAvailable = notAvailable
        }
        projectorsButtonState.value = currentProjectorButtonState
    }

    fun onKitchenShutter1ButtonClick(view: View) {
        val state: ShutterState? = kitchen1ShutterButtonState.value
        sendShutterRequest(state)
    }

    fun onKitchenShutter1LongButtonClick(view: View) {

    }

    fun onKitchenShutter2ButtonClick(view: View) {
        val state: ShutterState? = kitchen2ShutterButtonState.value
        sendShutterRequest(state)
    }

    fun onKitchenShutter2LongButtonClick(view: View) {

    }

    fun onRoomShutterButtonClick(view: View) {
        val state: ShutterState? = roomShutterButtonState.value
        sendShutterRequest(state)
    }

    fun onRoomShutterLongButtonClick(view: View) {

    }

    private fun sendShutterRequest(state: ShutterState?) {
        if (state?.notAvailable == null || state.notAvailable == true ||
            state.deviceName == null) {
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
                response = managerWebRepository.doShutter(state.deviceName, state.shutterNo, open)
            } catch (e: Throwable) {
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
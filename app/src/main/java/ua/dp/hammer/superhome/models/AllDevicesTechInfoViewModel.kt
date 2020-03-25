package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.AllStates
import ua.dp.hammer.superhome.data.DeviceTechInfo
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.data.PhoneAwareDeviceState
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.repositories.techinfo.AllDevicesTechInfoRepository

class AllDevicesTechInfoViewModel(private val localSettingsRepository: LocalSettingsRepository) : ViewModel() {
    private val allDevicesTechInfoRepository: AllDevicesTechInfoRepository = AllDevicesTechInfoRepository.getInstance()

    val devicesInfo: MutableLiveData<List<DeviceTechInfo>> = MutableLiveData()

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
            var oftenAmount = 0

            while (isActive) {
                try {
                    if (oftenAmount >= 3) {
                        delay(10_000)
                        oftenAmount = 0
                    }

                    val phoneAwareStates: MutableSet<PhoneAwareDeviceState> = HashSet()
                    devicesInfo.value?.forEach {
                        if (it.lastDeviceRequestTimestamp != null) {
                            phoneAwareStates.add(PhoneAwareDeviceState(it.deviceName, it.lastDeviceRequestTimestamp))
                        }
                    }

                    val requestStartTime = System.currentTimeMillis()
                    val response: List<DeviceTechInfo> = allDevicesTechInfoRepository.getAllDevicesTechInfoStates(phoneAwareStates)
                    val requestEndTime = System.currentTimeMillis()

                    setCustomNames(response)
                    devicesInfo.value = response

                    if ((requestEndTime - requestStartTime) < 1_000) {
                        // Too often
                        oftenAmount++
                    } else {
                        oftenAmount = 0
                    }
                } catch (e: Throwable) {
                    oftenAmount++
                }
            }
        }
    }

    private suspend fun setCustomNames(response: List<DeviceTechInfo>) {
        response.forEach {
            val customEnvSensorName = localSettingsRepository.getEnvSensorSettings(it.deviceName)

            if (customEnvSensorName?.displayedName != null) {
                it.displayedName = customEnvSensorName.displayedName
            } else {
                it.displayedName = it.deviceName
            }
        }
    }
}
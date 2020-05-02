package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.DeviceTechInfo
import ua.dp.hammer.superhome.data.DeviceType
import ua.dp.hammer.superhome.data.PhoneAwareDeviceState
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.repositories.techinfo.AllDevicesTechInfoRepository
import java.lang.IllegalArgumentException

class AllDevicesTechInfoViewModel(private val localSettingsRepository: LocalSettingsRepository) : ViewModel() {
    private val allDevicesTechInfoRepository: AllDevicesTechInfoRepository = AllDevicesTechInfoRepository.getInstance()

    val devicesInfo: MutableLiveData<MutableList<DeviceTechInfo>> = MutableLiveData()

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
                    val response: ArrayList<DeviceTechInfo> = allDevicesTechInfoRepository.getAllDevicesTechInfoStates(phoneAwareStates)
                    val requestEndTime = System.currentTimeMillis()

                    setCustomNames(response)
                    setResponseValues(response)

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

    private fun setResponseValues(response: ArrayList<DeviceTechInfo>) {
        val maxElementsAmount: Int = (devicesInfo.value?.size ?: 0) + response.size

        if (devicesInfo.value == null) {
            response.sortWith(DefaultSorter())
            devicesInfo.value = response
        } else {
            val newElementsCollection: MutableList<DeviceTechInfo> = ArrayList(maxElementsAmount)

            newElementsCollection.addAll(devicesInfo.value!!)

            for (responseElement in response) {
                val foundIndex = newElementsCollection.indexOfFirst{it.deviceName == responseElement.deviceName}

                if (foundIndex >= 0) {
                    newElementsCollection[foundIndex] = responseElement
                } else {
                    newElementsCollection.add(responseElement)
                }
            }
            newElementsCollection.sortWith(DefaultSorter())
            devicesInfo.value = newElementsCollection
        }
    }

    private inner class DefaultSorter : Comparator<DeviceTechInfo> {
        override fun compare(a: DeviceTechInfo, b: DeviceTechInfo): Int {
            if (a.notAvailable && !b.notAvailable) {
                return -1
            } else if (!a.notAvailable && b.notAvailable) {
                return 1
            } else {
                val typeComparison = compareByType(a.deviceType, b.deviceType)

                if (typeComparison != 0) {
                    return typeComparison
                } else {
                    return compareNullableNumbers(a.uptime, b.uptime)
                }
            }
        }
    }

    private val typesSortingOrder = mapOf(DeviceType.MOTION_DETECTOR to 1,
        DeviceType.SHUTTER to 2,
        DeviceType.PROJECTOR to 3,
        DeviceType.ENV_SENSOR to 4)

    private fun compareByType(a: DeviceType, b: DeviceType): Int {
        val aOrder = typesSortingOrder[a] ?: throw IllegalArgumentException("'$a' type is not known")
        val bOrder = typesSortingOrder[b] ?: throw IllegalArgumentException("'$b' type is not known")

        return if (aOrder < bOrder) {
            -1
        } else if (aOrder > bOrder) {
            1
        } else {
            0
        }
    }

    private fun compareNullableNumbers(a: Int?, b: Int?): Int {
        if (a == b) {
            return 0
        } else if (a == null && b != null) {
            return -1
        } else if (a != null && b == null) {
            return 1
        }

        return if (a!! < b!!) {
            -1
        } else {
            1
        }
    }
}
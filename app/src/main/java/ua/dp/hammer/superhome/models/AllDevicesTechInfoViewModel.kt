package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.DeviceTechInfo
import ua.dp.hammer.superhome.data.DeviceType
import ua.dp.hammer.superhome.data.PhoneAwareDeviceState
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.repositories.web.techinfo.AllDevicesTechInfoWebRepository

class AllDevicesTechInfoViewModel : AbstractMonitoringViewModel() {
    val devicesInfo: MutableLiveData<MutableList<DeviceTechInfo>> = MutableLiveData()

    private var allDevicesTechInfoWebRepository: AllDevicesTechInfoWebRepository? = null

    lateinit var localSettingsRepository: LocalSettingsRepository

    override fun setServerAddressAndInit(serverAddress: String) {
        if (notInitialized || allDevicesTechInfoWebRepository?.address != serverAddress) {
            allDevicesTechInfoWebRepository = AllDevicesTechInfoWebRepository(serverAddress)
            init()
        }
    }

    override fun startMonitoring(): Job {
        return viewModelScope.launch {
            var oftenAmount = 0

            while (isActive) {
                val requestStartTime = System.currentTimeMillis()
                var response: ArrayList<DeviceTechInfo>? = null

                try {
                    val phoneAwareStates: MutableSet<PhoneAwareDeviceState> = HashSet()
                    devicesInfo.value?.forEach {
                        if (it.lastDeviceRequestTimestamp != null) {
                            phoneAwareStates.add(PhoneAwareDeviceState(it.deviceName, it.lastDeviceRequestTimestamp))
                        }
                    }

                    response = allDevicesTechInfoWebRepository?.getAllDevicesTechInfoStates(phoneAwareStates)
                } catch (e: Throwable) {
                    oftenAmount++
                }

                val requestEndTime = System.currentTimeMillis()
                if ((requestEndTime - requestStartTime) < 1_000) {
                    // Too often
                    oftenAmount++
                } else {
                    oftenAmount = 0
                }

                if (response == null || oftenAmount >= 3) {
                    delay(10_000)
                    oftenAmount = 0
                    continue
                }

                setCustomNames(response)
                setResponseValues(response)
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
            if (a.notAvailable && !b.notAvailable || a.buildTimestamp?.isNotEmpty() == true) {
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
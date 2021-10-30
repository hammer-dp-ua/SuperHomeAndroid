package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.dp.hammer.superhome.data.setup.AlarmDisplayedOrderItemObservable
import ua.dp.hammer.superhome.repositories.web.setup.DevicesSetupWebRepository
import kotlin.streams.toList

class AlarmsDisplayedOrderViewModel : ViewModel() {
    val alarmsSources: MutableLiveData<Array<AlarmDisplayedOrderItemObservable>> = MutableLiveData()

    private var notInitialized = true
    private lateinit var devicesSetupWebRepository: DevicesSetupWebRepository

    fun setServerAddress(serverAddress: String) {
        if (notInitialized || devicesSetupWebRepository.address != serverAddress) {
            devicesSetupWebRepository = DevicesSetupWebRepository(serverAddress)
            notInitialized = false
        }
    }

    suspend fun loadAllAlarmSources() {
        alarmsSources.value = devicesSetupWebRepository.getAlarmSources()
            .stream()
            .map { AlarmDisplayedOrderItemObservable(it.deviceName, it.alarmSource) }
            .toList()
            .toTypedArray()
    }

    fun swap(sourceIndex: Int, destinationIndex: Int) {
        val sourceItem = alarmsSources.value?.get(sourceIndex)
        val destinationItem = alarmsSources.value?.get(destinationIndex)

        if (sourceItem != null && destinationItem != null) {
            alarmsSources.value?.set(sourceIndex, destinationItem)
            alarmsSources.value?.set(destinationIndex, sourceItem)
        }
    }
}
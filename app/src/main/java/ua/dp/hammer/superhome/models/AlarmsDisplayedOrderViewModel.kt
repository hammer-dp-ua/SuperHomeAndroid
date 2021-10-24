package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.dp.hammer.superhome.data.setup.AlarmDisplayedOrderItemObservable
import ua.dp.hammer.superhome.repositories.web.setup.DevicesSetupWebRepository
import kotlin.streams.toList

class AlarmsDisplayedOrderViewModel : ViewModel() {
    val alarmsSources: MutableLiveData<List<AlarmDisplayedOrderItemObservable>> = MutableLiveData()

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
    }
}
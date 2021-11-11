package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.setup.AlarmSourceSetupObservable
import ua.dp.hammer.superhome.repositories.web.setup.DevicesSetupWebRepository
import ua.dp.hammer.superhome.transport.DeviceSetupTransport
import kotlin.streams.toList

class AlarmSourcesSetupViewModel : ViewModel() {
    val alarmSources: MutableLiveData<MutableList<AlarmSourceSetupObservable>> = MutableLiveData()

    private var notInitialized = true
    private lateinit var devicesSetupWebRepository: DevicesSetupWebRepository

    fun setServerAddress(serverAddress: String) {
        if (notInitialized || devicesSetupWebRepository.address != serverAddress) {
            devicesSetupWebRepository = DevicesSetupWebRepository(serverAddress)
            notInitialized = false
        }
    }

    fun clearAllAlarmSourcesBeforeLoadingNew() {
        alarmSources.value?.clear()
    }

    fun loadAllAlarmSources() {
        viewModelScope.launch {
            val loadedAlarmSources = devicesSetupWebRepository.getAlarmSources()
            val loadedAlarmSourcesObservableList = mutableListOf<AlarmSourceSetupObservable>()

            for (loadedAlarmSource in loadedAlarmSources) {
                loadedAlarmSourcesObservableList.add(AlarmSourceSetupObservable(loadedAlarmSource))
            }
            alarmSources.value = loadedAlarmSourcesObservableList
        }
    }

    fun save() {
        for (alarmSourceSetupObservable in alarmSources.value.orEmpty()) {
            if (alarmSourceSetupObservable.canBeSaved()) {
                if (alarmSourceSetupObservable.isNew()) {
                    viewModelScope.launch {
                        devicesSetupWebRepository.addAlarmSource(alarmSourceSetupObservable.createTransport())
                    }
                } else if (alarmSourceSetupObservable.wasChanged()) {
                    viewModelScope.launch {
                        devicesSetupWebRepository.modifyAlarmSource(alarmSourceSetupObservable.createTransport())
                    }
                }
            }
        }
    }

    fun addNew() {
        viewModelScope.launch {
            val allDevices = devicesSetupWebRepository.getAllDevices()
            val allDevicesNames: List<String> = allDevices
                .stream()
                .map(DeviceSetupTransport::name)
                .toList()
            val newAlarmSource = AlarmSourceSetupObservable(allDevicesNames)
            val newAlarmSources = mutableListOf(newAlarmSource)
            val currentAlarmSourcesIterator = alarmSources.value?.asIterable()

            if (currentAlarmSourcesIterator != null) {
                newAlarmSources.addAll(currentAlarmSourcesIterator)
            }

            alarmSources.value = newAlarmSources
        }
    }

    fun delete(item: AlarmSourceSetupObservable): Int {
        val itemIndex = alarmSources.value.orEmpty().indexOf(item)

        if (itemIndex >= 0) {
            alarmSources.value?.remove(item)

            if (item.aaId != null) {
                viewModelScope.launch {
                    devicesSetupWebRepository.deleteAlarmSource(item.aaId)
                }
            }
        }
        return itemIndex
    }
}
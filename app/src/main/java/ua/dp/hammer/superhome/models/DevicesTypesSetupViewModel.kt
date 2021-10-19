package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.setup.DeviceTypeSetupObservable
import ua.dp.hammer.superhome.db.entities.DeviceDisplayedTypeEntity
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.repositories.web.setup.DevicesSetupWebRepository

class DevicesTypesSetupViewModel : ViewModel() {
    val types: MutableLiveData<MutableList<DeviceTypeSetupObservable>> = MutableLiveData()

    lateinit var localSettingsRepository: LocalSettingsRepository

    private var notInitialized = true
    private lateinit var devicesSetupWebRepository: DevicesSetupWebRepository

    fun setServerAddress(serverAddress: String) {
        if (notInitialized || devicesSetupWebRepository.address != serverAddress) {
            devicesSetupWebRepository = DevicesSetupWebRepository(serverAddress)
            notInitialized = false
        }
    }

    fun loadAllTypes() {
        viewModelScope.launch {
            val loadedTypes = devicesSetupWebRepository.getAllDeviceTypes()
            val loadedTypesObservableList = mutableListOf<DeviceTypeSetupObservable>()

            for (loadedType in loadedTypes) {
                val displayedTypeEntity = localSettingsRepository.getDeviceDisplayedType(loadedType.type)
                loadedTypesObservableList.add(DeviceTypeSetupObservable(loadedType, displayedTypeEntity?.displayedType))
            }
            types.value = loadedTypesObservableList
        }
    }

    fun save() {
        for (deviceType : DeviceTypeSetupObservable in types.value.orEmpty()) {
            if (deviceType.isToBeSaved()) {
                viewModelScope.launch {
                    devicesSetupWebRepository.saveDeviceType(deviceType.createTransport())
                }
            }

            val type = deviceType.type.value

            if (deviceType.wasDisplayedTypeModified() && !type.isNullOrEmpty()) {
                viewModelScope.launch {
                    val deviceDisplayedTypeEntity = DeviceDisplayedTypeEntity(type, deviceType.displayedType.value)
                    localSettingsRepository.saveDeviceDisplayedType(deviceDisplayedTypeEntity)
                }
            }
        }
    }

    fun addNew() {
        val newDeviceType = DeviceTypeSetupObservable()
        val newDevicesTypes = mutableListOf(newDeviceType)
        val currentDevicesIterator = types.value?.asIterable()

        if (currentDevicesIterator != null) {
            newDevicesTypes.addAll(currentDevicesIterator)
        }

        types.value = newDevicesTypes
    }

    fun delete(item: DeviceTypeSetupObservable): Int {
        val itemIndex = types.value.orEmpty().indexOf(item)

        if (itemIndex >= 0) {
            types.value?.remove(item)

            viewModelScope.launch {
                devicesSetupWebRepository.deleteDeviceType(item.createTransport())
            }
        }
        return itemIndex
    }
}
package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.DeviceSetupInfo
import ua.dp.hammer.superhome.repositories.web.setup.DevicesSetupWebRepository

class DevicesSetupViewModel : ViewModel() {
    val devices: MutableLiveData<List<DeviceSetupInfo>> = MutableLiveData()

    private var notInitialized = true
    lateinit private var devicesSetupWebRepository: DevicesSetupWebRepository

    fun setServerAddress(serverAddress: String) {
        if (notInitialized || devicesSetupWebRepository.address != serverAddress) {
            devicesSetupWebRepository = DevicesSetupWebRepository(serverAddress)
            notInitialized = false
        }
    }

    fun loadAllDevices() {
        viewModelScope.launch {
            devices.value = devicesSetupWebRepository.getAllDevices()
        }
    }
}
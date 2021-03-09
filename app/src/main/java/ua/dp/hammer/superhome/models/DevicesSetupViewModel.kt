package ua.dp.hammer.superhome.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.DeviceSetupObservable
import ua.dp.hammer.superhome.repositories.web.setup.DevicesSetupWebRepository
import ua.dp.hammer.superhome.transport.DeviceSetupTransport

class DevicesSetupViewModel : ViewModel() {
    val devices: MutableLiveData<MutableList<DeviceSetupObservable>> = MutableLiveData()

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
            val loadedDevices = devicesSetupWebRepository.getAllDevices()
            val loadedDevicesObservable = mutableListOf<DeviceSetupObservable>()

            for (loadedDevice in loadedDevices) {
                loadedDevicesObservable.add(DeviceSetupObservable(loadedDevice))
            }
            devices.value = loadedDevicesObservable
        }
    }

    fun save() {
        val toBeSavedDevices = mutableListOf<DeviceSetupTransport>()

        for (device : DeviceSetupObservable in devices.value.orEmpty()) {
            if (device.isToBeSaved()) {
                toBeSavedDevices.add(device.createTransport())
            }
        }

        Log.i(null, "To be saved devices amount: " + toBeSavedDevices.size)
    }

    fun deleteById(id: String) {
        delete(id, null)
    }

    fun deleteByName(name: String) {
        delete(null, name)
    }

    private fun delete(id: String?, name: String?) {
        if (!id.isNullOrEmpty()) {
            val foundDevice = devices.value?.stream()?.filter {
                it.id.value == id
            }?.findFirst()?.orElse(null)

            if (foundDevice != null) {
                viewModelScope.launch {
                    //devicesSetupWebRepository.deleteDevice(id.toInt())
                    //loadAllDevices()
                    Log.i(null, "Deleting setting device: $id")
                }
            }
        } else if (!name.isNullOrEmpty()) {
            val foundDevice = devices.value?.stream()?.filter {
                it.name.value == name
            }?.findFirst()?.orElse(null)

            if (foundDevice != null) {
                devices.value?.remove(foundDevice)
            }
        }
    }
}
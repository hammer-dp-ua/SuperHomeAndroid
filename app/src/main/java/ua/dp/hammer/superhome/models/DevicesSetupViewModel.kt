package ua.dp.hammer.superhome.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.DeviceSetupObservable
import ua.dp.hammer.superhome.data.DeviceTypeSetupInfo
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

    fun addNew() {
        val newDevice = DeviceSetupObservable()
        newDevice.type.value = DeviceTypeSetupInfo.ENV_SENSOR
        val newDevices = mutableListOf(newDevice)
        val currentDevicesIterator = devices.value?.asIterable()

        if (currentDevicesIterator != null) {
            newDevices.addAll(currentDevicesIterator)
        }

        devices.value = newDevices
    }

    fun deleteById(id: String): Int {
        return delete(id, null)
    }

    fun delete(item: DeviceSetupObservable): Int {
        return delete(null, item)
    }

    private fun delete(id: String?, item: DeviceSetupObservable?): Int {
        var itemIndex = 0


        if (!id.isNullOrEmpty()) {
            var foundDevice : DeviceSetupObservable? = null

            for (collectionDevice in devices.value.orEmpty()) {
                if (collectionDevice.id.value == id) {
                    foundDevice = collectionDevice
                    break
                }
                itemIndex++
            }

            if (foundDevice == null) {
                return -1
            }

            viewModelScope.launch {
                //devicesSetupWebRepository.deleteDevice(id.toInt())
                loadAllDevices()
                Log.i(null, "Deleting setting device: $id")
            }
            return itemIndex
        } else if (item != null) {
            itemIndex = devices.value.orEmpty().indexOf(item)

            if (itemIndex >= 0) {
                devices.value?.remove(item)
            }
            return itemIndex
        }
        return -1
    }
}
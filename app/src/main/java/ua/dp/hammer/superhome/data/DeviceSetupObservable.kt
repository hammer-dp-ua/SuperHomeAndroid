package ua.dp.hammer.superhome.data

import androidx.lifecycle.MutableLiveData
import ua.dp.hammer.superhome.transport.DeviceSetupTransport

class DeviceSetupObservable  {
    constructor(transport: DeviceSetupTransport) {
        id.value = transport.id?.toString()
        type.value = transport.type
        name.value = transport.name
        keepAliveInterval.value = transport.keepAliveIntervalSec?.toString()
        ip4Address.value = transport.ip4Address ?: ""
        saveInitState()
    }

    constructor()

    val id = MutableLiveData<String>()
    val type = MutableLiveData<DeviceTypeSetupInfo>()
    val name = MutableLiveData<String>()
    val keepAliveInterval = MutableLiveData<String>()
    val ip4Address = MutableLiveData<String>()

    var initState = 0

    fun createTransport(): DeviceSetupTransport {
        return DeviceSetupTransport(id.value?.toInt(),
            type.value ?: throw IllegalStateException("Type can't be null"),
            name.value ?: throw IllegalStateException("Name can't be null"),
            null,
            ip4Address.value
        )
    }

    private fun saveInitState() {
        initState = hashCode()
    }

    private fun wasModified(): Boolean {
        return initState != hashCode()
    }

    fun isToBeSaved(): Boolean {
        return (id.value == null || wasModified()) &&
                !name.value.isNullOrEmpty()
    }

    override fun hashCode(): Int {
        var result = 17

        result = 31 * result + id.value.hashCode()
        result = 31 * result + name.value.hashCode()
        result = 31 * result + keepAliveInterval.value.hashCode()
        result = 31 * result + ip4Address.value.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other !is DeviceSetupObservable) {
            return false
        }

        val thatObject: DeviceSetupObservable = other
        return id.value == thatObject.id.value &&
                name.value == thatObject.name.value &&
                type.value == thatObject.type.value &&
                keepAliveInterval.value == thatObject.keepAliveInterval.value &&
                ip4Address.value == thatObject.ip4Address.value
    }
}
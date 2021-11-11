package ua.dp.hammer.superhome.data.setup

import androidx.lifecycle.MutableLiveData
import ua.dp.hammer.superhome.transport.DeviceTypeSetupTransport

class DeviceTypeSetupObservable {
    constructor(transport: DeviceTypeSetupTransport,
                displayedType: String?) {
        this.type.value = transport.type
        this.displayedType.value = displayedType
        this.keepAliveInterval.value = transport.keepAliveIntervalSec.toString()

        saveInitState()
    }

    constructor()

    val type = MutableLiveData<String>()
    val displayedType = MutableLiveData<String>()
    val keepAliveInterval = MutableLiveData<String>()

    var initState = 0
    var initDisplayedType: String? = null

    fun isNew(): Boolean {
        return initState == 0
    }

    fun createTransport(): DeviceTypeSetupTransport {
        return DeviceTypeSetupTransport(type.value ?: throw IllegalStateException("Type can't be null"),
            keepAliveInterval.value?.toInt() ?: throw IllegalStateException("Keep alive interval can't be null"))
    }

    private fun saveInitState() {
        initState = hashCode()
        initDisplayedType = displayedType.value
    }

    fun isToBeSaved(): Boolean {
        return wasModified() && !type.value.isNullOrEmpty() && !keepAliveInterval.value.isNullOrEmpty()
    }

    private fun wasModified(): Boolean {
        return initState != hashCode()
    }

    fun wasDisplayedTypeModified(): Boolean {
        return initDisplayedType != displayedType.value
    }

    override fun toString(): String {
        return DeviceTypeSetupObservable::class.java.simpleName + "@" + super.hashCode() +
                ". type: ${type.value}, displayedType: ${displayedType.value}, keepAliveInterval: ${keepAliveInterval.value}"
    }

            override fun equals(other: Any?): Boolean {
        if (other !is DeviceTypeSetupObservable) {
            return false
        }

        val thatObject: DeviceTypeSetupObservable = other
        return type.value == thatObject.type.value &&
                keepAliveInterval.value == thatObject.keepAliveInterval.value
    }

    override fun hashCode(): Int {
        var result = 17

        result = 31 * result + type.value.hashCode()
        result = 31 * result + keepAliveInterval.value.hashCode()
        return result
    }
}
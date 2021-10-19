package ua.dp.hammer.superhome.data.setup

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import ua.dp.hammer.superhome.transport.DeviceSetupTransport

class DeviceSetupObservable {
    constructor(transport: DeviceSetupTransport,
                types: List<String>,
                displayedTypes: List<String>) {
        id.value = transport.id?.toString()
        name.value = transport.name
        keepAliveInterval.value = transport.keepAliveIntervalSec.toString()
        ip4Address.value = transport.ip4Address ?: ""

        type = transport.type
        this.types = types
        this.displayedTypes = displayedTypes
        saveInitState()
    }

    constructor()

    val id = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val keepAliveInterval = MutableLiveData<String>()
    val ip4Address = MutableLiveData<String>()

    lateinit var types: List<String>
    lateinit var type: String
    lateinit var displayedTypes: List<String>

    private var initState = 0

    fun createTransport(): DeviceSetupTransport {
        return DeviceSetupTransport(id.value?.toInt(),
            type,
            name.value ?: throw IllegalStateException("Name can't be null"),
            keepAliveInterval.value?.toInt() ?: 0,
            ip4Address.value
        )
    }

    fun onSelectType(parent: AdapterView<*>?, view: View, pos: Int, id: Long) {
        type = types[pos]
    }

    private fun saveInitState() {
        initState = hashCode()
    }

    private fun wasModified(): Boolean {
        return initState != hashCode()
    }

    fun isToBeSaved(): Boolean {
        return (id.value == null || wasModified()) && !name.value.isNullOrEmpty()
    }

    override fun hashCode(): Int {
        var result = 17

        result = 31 * result + id.value.hashCode()
        result = 31 * result + type.hashCode()
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
                type == thatObject.type &&
                keepAliveInterval.value == thatObject.keepAliveInterval.value &&
                ip4Address.value == thatObject.ip4Address.value
    }

    override fun toString(): String {
        return "id: ${id.value}, type: $type, name: ${name.value}, keepAliveInterval: ${keepAliveInterval.value}, " +
                "ip4Address: ${ip4Address.value}"
    }
}
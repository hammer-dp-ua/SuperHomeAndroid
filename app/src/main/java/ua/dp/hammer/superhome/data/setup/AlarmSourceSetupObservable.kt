package ua.dp.hammer.superhome.data.setup

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import ua.dp.hammer.superhome.transport.AlarmSourceSetupTransport

class AlarmSourceSetupObservable {
    constructor(transport: AlarmSourceSetupTransport) {
        this.aaId = transport.aaId
        deviceName.value = transport.deviceName
        alarmSource.value = transport.alarmSource
        ignoreAlarms.value = transport.ignoreAlarms
        this.initIgnoreAlarmsValue = transport.ignoreAlarms
    }

    constructor(devicesNames: List<String>) {
        this.aaId = null
        this.initIgnoreAlarmsValue = false
        ignoreAlarms.value = this.initIgnoreAlarmsValue

        this.devicesNames.add("")
        this.devicesNames.addAll(devicesNames)
    }

    val deviceName = MutableLiveData<String>()
    val alarmSource = MutableLiveData<String>()
    val ignoreAlarms = MutableLiveData<Boolean>()

    val aaId: Int?
    val devicesNames = mutableListOf<String>()

    private val initIgnoreAlarmsValue: Boolean

    fun isNew(): Boolean {
        return aaId == null
    }

    fun wasChanged(): Boolean {
        return (aaId != null) && (initIgnoreAlarmsValue != ignoreAlarms.value)
    }

    fun canBeSaved(): Boolean {
        return !deviceName.value.isNullOrEmpty() && !alarmSource.value.isNullOrEmpty() &&
                ignoreAlarms.value != null
    }

    fun createTransport(): AlarmSourceSetupTransport {
        return AlarmSourceSetupTransport(
            aaId,
            deviceName.value ?: throw IllegalStateException("Name can't be null"),
            alarmSource.value,
            ignoreAlarms.value ?: throw IllegalStateException("Ignore alarms can't be null")
        )
    }

    fun onSelectDeviceName(parent: AdapterView<*>?, view: View, pos: Int, id: Long) {
        deviceName.value = devicesNames[pos]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlarmSourceSetupObservable

        if (deviceName.value != other.deviceName.value) return false
        if (alarmSource.value != other.alarmSource.value) return false
        if (ignoreAlarms.value != other.ignoreAlarms.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = deviceName.value.hashCode()
        result = 31 * result + alarmSource.value.hashCode()
        result = 31 * result + ignoreAlarms.value.hashCode()
        return result
    }
}
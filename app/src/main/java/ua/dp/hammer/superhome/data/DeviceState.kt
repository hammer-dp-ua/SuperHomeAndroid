package ua.dp.hammer.superhome.data

open class DeviceState(
    val deviceName: String,
    val gain: Int?,
    val errors: Int?,
    val uptime: Int?,
    val freeHeapSpace: Int?,
    val type: DeviceType,
    val lastKeepAliveRequestSec: Int) {
    constructor() : this("", null, null, null, null, DeviceType.ENV_SENSOR, 0)

    fun getGainString(): String? {
        return gain?.toString()?.plus("dB")
    }

    fun getErrorsString(): String? {
        return errors?.toString()
    }

    fun getUptimeString(): String? {
        return uptime?.toString()
    }

    fun getFreeHeapSpaceString(): String? {
        return freeHeapSpace?.toString()
    }
}
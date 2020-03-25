package ua.dp.hammer.superhome.data

open class DeviceState(
    val deviceName: String,
    val gain: Int?,
    val errors: Int?,
    val uptime: Int?,
    val freeHeapSpace: Int?) {
    constructor() : this("", null, null, null, null)

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
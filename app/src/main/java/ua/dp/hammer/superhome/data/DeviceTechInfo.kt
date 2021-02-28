package ua.dp.hammer.superhome.data

import java.text.SimpleDateFormat
import java.util.*

data class DeviceTechInfo(
    val lastDeviceRequestTimestamp: Long?,
    val buildTimestamp: String?,
    val notAvailable: Boolean,
    val deviceType: DeviceType
) : DeviceState() {
    lateinit var displayedName: String

    fun getLastDeviceRequestTimestampString(): String? {
        if (lastDeviceRequestTimestamp == null) {
            return null
        }
        return SimpleDateFormat("dd HH:mm:ss", Locale.getDefault()).format(Date(lastDeviceRequestTimestamp))
    }
}
package ua.dp.hammer.superhome.data

data class DeviceSetupInfo(
    val id: Int?,
    val type: DeviceTypeSetupInfo,
    val name: String,
    val keepAliveIntervalSec: Int?,
    val ip4Address: String?
)
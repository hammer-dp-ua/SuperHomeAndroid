package ua.dp.hammer.superhome.transport

import ua.dp.hammer.superhome.data.DeviceTypeSetupInfo

data class DeviceSetupTransport(
    val id: Int?,
    val type: DeviceTypeSetupInfo,
    val name: String,
    val keepAliveIntervalSec: Int?,
    val ip4Address: String?
)
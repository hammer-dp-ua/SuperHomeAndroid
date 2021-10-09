package ua.dp.hammer.superhome.transport

data class DeviceSetupTransport(
    val id: Int?,
    val type: String,
    val name: String,
    val keepAliveIntervalSec: Int,
    val ip4Address: String?
)
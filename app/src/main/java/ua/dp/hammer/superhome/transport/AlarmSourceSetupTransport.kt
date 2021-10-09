package ua.dp.hammer.superhome.transport

data class AlarmSourceSetupTransport(
    val deviceName: String,
    val alarmSource: String?,
    val ignoreAlarms: Boolean
)
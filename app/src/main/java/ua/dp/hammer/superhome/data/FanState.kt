package ua.dp.hammer.superhome.data

data class FanState(
    var deviceName: String? = null,
    var turnedOn: Boolean? = null,
    val minutesRemaining: Int? = null,
    var notAvailable: Boolean? = null)
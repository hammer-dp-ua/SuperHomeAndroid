package ua.dp.hammer.superhome.data

data class FanState(
    var turnedOn: Boolean = false,
    val minutesRemaining: Int = 0,
    var notAvailable: Boolean = true)
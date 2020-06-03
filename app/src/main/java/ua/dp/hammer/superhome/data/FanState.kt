package ua.dp.hammer.superhome.data

data class FanState(
    var turnedOn: Boolean? = null,
    val minutesRemaining: Int? = null,
    var notAvailable: Boolean? = null)
package ua.dp.hammer.superhome.data

data class ShutterState(
    val deviceName: String? = null,
    val shutterNo: Int = 0,
    var state: ShutterStates = ShutterStates.SHUTTER_CLOSED,
    var notAvailable: Boolean? = null
)
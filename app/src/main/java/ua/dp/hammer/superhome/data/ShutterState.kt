package ua.dp.hammer.superhome.data

data class ShutterState(
    val name: String,
    val shutterNo: Int,
    var state: ShutterStates,
    var notAvailable: Boolean
) {
    constructor() : this("", 0, ShutterStates.SHUTTER_CLOSED, true)
}
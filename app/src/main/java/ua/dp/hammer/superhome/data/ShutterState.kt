package ua.dp.hammer.superhome.data

data class ShutterState(
    val name: String,
    val shutterNo: Int,
    val state: ShutterStates,
    val notAvailable: Boolean
)
package ua.dp.hammer.superhome.data

data class FanSettingsInfo(
    val name: String,
    val turnOnHumidityThreshold: Float,
    val manuallyTurnedOnTimeoutMinutes: Int,
    val afterFallingThresholdWorkTimeoutMinutes: Int)
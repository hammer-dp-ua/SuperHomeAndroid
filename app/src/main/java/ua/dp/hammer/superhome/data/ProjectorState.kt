package ua.dp.hammer.superhome.data

data class ProjectorState(val deviceName: String? = null,
                          var turnedOn: Boolean = false,
                          var notAvailable: Boolean? = null)
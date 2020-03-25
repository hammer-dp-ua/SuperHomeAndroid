package ua.dp.hammer.superhome.data

data class ProjectorState(val deviceName: String,
                          var turnedOn: Boolean,
                          var notAvailable: Boolean) {
    constructor() : this("", false, true)
}
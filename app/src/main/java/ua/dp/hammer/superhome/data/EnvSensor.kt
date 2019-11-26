package ua.dp.hammer.superhome.data

data class EnvSensor(
    val deviceName: String,
    val temperature: Float?,
    val humidity: Float?,
    val light: Int?,
    val gain: Int?,
    val errors: Int?,
    val uptime: Int?,
    val freeHeapSpace: Int?
) {

    fun getTemperatureString(): String? {
        return temperature?.toString()?.plus("°C")
    }

    fun getHumidityString(): String? {
        return humidity?.toString()?.plus("%")
    }

    fun getLightString(): String? {
        return light?.toString()?.plus("%")
    }

    fun getGainString(): String? {
        return gain?.toString()?.plus("dB")
    }
}
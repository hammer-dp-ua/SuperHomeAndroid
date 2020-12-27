package ua.dp.hammer.superhome.data

import android.view.View
import androidx.lifecycle.MutableLiveData

data class EnvSensor(
    val temperature: Float?,
    val humidity: Float?,
    val light: Int?
) : DeviceState() {
    lateinit var displayedName: MutableLiveData<String>
    lateinit var temperatureVisibility: MutableLiveData<Int>
    lateinit var humidityVisibility: MutableLiveData<Int>
    lateinit var humidityIsIllegal: MutableLiveData<Boolean>
    lateinit var lightVisibility: MutableLiveData<Int>
    lateinit var gainVisibility: MutableLiveData<Int>
    lateinit var errorsVisibility: MutableLiveData<Int>
    lateinit var uptimeVisibility: MutableLiveData<Int>
    lateinit var freeHeapSpaceVisibility: MutableLiveData<Int>

    private val humidityIsIllegalInternal = humidity != null && humidity >= 150.0

    fun initObservables() {
        displayedName = MutableLiveData(deviceName)
        temperatureVisibility = MutableLiveData(View.GONE)
        humidityVisibility = MutableLiveData(View.GONE)
        humidityIsIllegal = MutableLiveData(humidityIsIllegalInternal)
        lightVisibility = MutableLiveData(View.GONE)
        gainVisibility = MutableLiveData(View.GONE)
        errorsVisibility = MutableLiveData(View.GONE)
        uptimeVisibility = MutableLiveData(View.GONE)
        freeHeapSpaceVisibility = MutableLiveData(View.GONE)
    }

    fun getTemperatureString(): String? {
        return roundFloat(temperature)?.plus("°C")
    }

    fun getHumidityString(): String? {
        val humidityString: String? = if (humidityIsIllegalInternal) {
            "0.0"
        } else {
            roundFloat(humidity)
        }
        return humidityString.plus("%")
    }

    fun getLightString(): String? {
        return light?.toString()?.plus("%")
    }

    private fun roundFloat(floatValue: Float?): String? {
        if (floatValue == null) {
            return null
        }
        return String.format("%.1f", floatValue)
    }
}
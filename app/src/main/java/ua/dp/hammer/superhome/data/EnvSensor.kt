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
    lateinit var lightVisibility: MutableLiveData<Int>
    lateinit var gainVisibility: MutableLiveData<Int>
    lateinit var errorsVisibility: MutableLiveData<Int>
    lateinit var uptimeVisibility: MutableLiveData<Int>
    lateinit var freeHeapSpaceVisibility: MutableLiveData<Int>

    fun initObservables() {
        displayedName = MutableLiveData(deviceName)
        temperatureVisibility = MutableLiveData(View.GONE)
        humidityVisibility = MutableLiveData(View.GONE)
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
        return roundFloat(humidity)?.plus("%")
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
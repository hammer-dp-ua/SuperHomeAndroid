package ua.dp.hammer.superhome.data

import android.view.View
import androidx.lifecycle.MutableLiveData

data class EnvSensorDisplayedInfo(
    val name: MutableLiveData<String> = MutableLiveData(),
    val displayedName: MutableLiveData<String> = MutableLiveData(),
    val isTemperatureDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val temperatureOptionVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE),
    val isHumidityDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val humidityOptionVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE),
    val isLightDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val lightOptionVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE),
    val isGainDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val gainOptionVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE),
    val areErrorsDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val errorsOptionVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE),
    val isUptimeDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val uptimeOptionVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE),
    val isFreeHeapSpaceDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val freeHeapSpaceOptionVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
) {
    override fun toString(): String {
        return "Name: ${name.value}, Displayed name: ${displayedName.value}, temp: ${isTemperatureDisplayed.value}, " +
                "humidity: ${isHumidityDisplayed.value}, light: ${isLightDisplayed.value}, gain: ${isGainDisplayed.value}, " +
                "errors: ${areErrorsDisplayed.value}, uptime: ${isUptimeDisplayed.value}, free heap: ${isFreeHeapSpaceDisplayed.value}"
    }
}
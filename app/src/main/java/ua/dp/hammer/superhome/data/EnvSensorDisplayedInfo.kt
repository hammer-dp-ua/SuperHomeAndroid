package ua.dp.hammer.superhome.data

import androidx.lifecycle.MutableLiveData

data class EnvSensorDisplayedInfo(
    val name: String,
    val displayedName: MutableLiveData<String> = MutableLiveData(),
    val isTemperatureDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val isHumidityDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val isLightDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val isGainDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val areErrorsDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val isUptimeDisplayed: MutableLiveData<Boolean> = MutableLiveData(),
    val isFreeHeapSpaceDisplayed: MutableLiveData<Boolean> = MutableLiveData()
)
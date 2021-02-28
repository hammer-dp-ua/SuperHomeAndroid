package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.FanSettingsInfo
import ua.dp.hammer.superhome.repositories.web.settings.ServerSettingsWebRepository

class FanSettingsViewModel(private val serverSettingsWebRepository: ServerSettingsWebRepository) : ViewModel() {
    val name: MutableLiveData<String> = MutableLiveData()
    val turnOnHumidityThreshold: MutableLiveData<Int> = MutableLiveData()
    val manuallyTurnedOnTimeoutMinutes: MutableLiveData<Int> = MutableLiveData()
    val afterFallingThresholdWorkTimeoutMinutes: MutableLiveData<Int> = MutableLiveData()

    fun saveSettings() {
        val turnOnHumidityThresholdValue: Float = turnOnHumidityThreshold.value!!.toFloat()
        val fanSettingsInfo = FanSettingsInfo(name.value!!, turnOnHumidityThresholdValue,
            manuallyTurnedOnTimeoutMinutes.value!!, afterFallingThresholdWorkTimeoutMinutes.value!!)

        viewModelScope.launch {
            serverSettingsWebRepository.saveFanSettings(fanSettingsInfo)
        }
    }

    fun loadSettings(fanName: String) {
        viewModelScope.launch {
            val fanSettingsInfo = serverSettingsWebRepository.getFanSettings(fanName)

            name.value = fanSettingsInfo.name
            turnOnHumidityThreshold.value = fanSettingsInfo.turnOnHumidityThreshold.toInt()
            manuallyTurnedOnTimeoutMinutes.value = fanSettingsInfo.manuallyTurnedOnTimeoutMinutes
            afterFallingThresholdWorkTimeoutMinutes.value = fanSettingsInfo.afterFallingThresholdWorkTimeoutMinutes
        }
    }
}
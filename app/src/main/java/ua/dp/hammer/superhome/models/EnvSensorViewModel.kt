package ua.dp.hammer.superhome.models

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.data.EnvSensorDisplayedInfo
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRow
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRow.RowNames.*
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingAndDisplayedRows
import ua.dp.hammer.superhome.repositories.sensors.EnvSensorsRepository
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

class EnvSensorViewModel(private val localSettingsRepository: LocalSettingsRepository) : ViewModel() {
    val sensors: MutableLiveData<List<EnvSensor>> = MutableLiveData()
    var stopUpdating = false

    private val envSensorsRepository: EnvSensorsRepository = EnvSensorsRepository.getInstance()

    init {
        viewModelScope.launch {
            var success = false

            while (!success) {
                try {
                    val response: List<EnvSensor> = envSensorsRepository.getEnvSensorsValuesAsync()

                    applyColumnsVisibility(response)
                    sensors.value = response
                    success = true
                } catch (e: Throwable) {
                    delay(10_000)
                }
            }

            var oftenAmount = 0

            while (isActive) {
                try {
                    val requestStartTime = System.currentTimeMillis()
                    val response = envSensorsRepository.getAllEnvSensorsDataDeferredAsync()
                    val requestEndTime = System.currentTimeMillis()

                    if (!stopUpdating) {
                        applyColumnsVisibility(response)
                        sensors.value = response
                    }

                    if ((requestEndTime - requestStartTime) < 1_000) {
                        // Too often
                        oftenAmount++
                    } else {
                        oftenAmount = 0
                    }

                    if (oftenAmount >= 3) {
                        delay(10_000)
                        oftenAmount = 0;
                    }
                } catch (e: Throwable) {
                    delay(10_000)
                }
            }
        }
    }

    fun updateVisibility(detachedDisplayedInfo: EnvSensorDisplayedInfo.Detached) {
        val envSensor = sensors.value?.firstOrNull {
            it.deviceName == detachedDisplayedInfo.name
        } ?: return

        viewModelScope.launch {
            if (detachedDisplayedInfo.displayedName != null) {
                envSensor.displayedName.value = detachedDisplayedInfo.displayedName
            }
            envSensor.temperatureVisibility.value = getVisibility(detachedDisplayedInfo.isTemperatureDisplayed)
            envSensor.humidityVisibility.value = getVisibility(detachedDisplayedInfo.isHumidityDisplayed)
            envSensor.lightVisibility.value = getVisibility(detachedDisplayedInfo.isLightDisplayed)
            envSensor.gainVisibility.value = getVisibility(detachedDisplayedInfo.isGainDisplayed)
            envSensor.errorsVisibility.value = getVisibility(detachedDisplayedInfo.areErrorsDisplayed)
            envSensor.uptimeVisibility.value = getVisibility(detachedDisplayedInfo.isUptimeDisplayed)
            envSensor.freeHeapSpaceVisibility.value = getVisibility(detachedDisplayedInfo.isFreeHeapSpaceDisplayed)
        }
    }

    private fun getVisibility(isDisplayed: Boolean?): Int {
        return when (isDisplayed) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    private suspend fun applyColumnsVisibility(envSensors: List<EnvSensor>) {
        for (envSensor in envSensors) {
            val settingsAndRows: EnvSensorSettingAndDisplayedRows? =
                localSettingsRepository.getEnvSensorSettingsAndDisplayedRows(envSensor.deviceName)
            val displayedName = settingsAndRows?.envSensorSettings?.displayedName
            val displayedRows = settingsAndRows?.displayedRows

            envSensor.initObservables()

            if (displayedName != null) {
                envSensor.displayedName.value = displayedName
            }

            envSensor.temperatureVisibility.value =
                getVisibility(envSensor.temperature == null, TEMPERATURE, displayedRows)
            envSensor.humidityVisibility.value =
                getVisibility(envSensor.humidity == null, HUMIDITY, displayedRows)
            envSensor.lightVisibility.value =
                getVisibility(envSensor.light == null, LIGHT, displayedRows)
            envSensor.gainVisibility.value =
                getVisibility(envSensor.gain == null, GAIN, displayedRows)
            envSensor.errorsVisibility.value =
                getVisibility(envSensor.errors == null, ERRORS, displayedRows)
            envSensor.uptimeVisibility.value =
                getVisibility(envSensor.uptime == null, UPTIME, displayedRows)
            envSensor.freeHeapSpaceVisibility.value =
                getVisibility(envSensor.freeHeapSpace == null, FREE_HEAP, displayedRows)
        }
    }

    private fun getVisibility(isValueNull: Boolean,
                              rowName: EnvSensorDisplayedRow.RowNames,
                              displayedRows: List<EnvSensorDisplayedRow>?): Int {
        if (isValueNull) {
            return View.GONE
        }

        return when ((displayedRows == null) || (displayedRows.firstOrNull {
            it.rowName == rowName.name
        } != null)) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }
}
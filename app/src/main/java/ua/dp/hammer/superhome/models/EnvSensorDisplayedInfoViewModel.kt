package ua.dp.hammer.superhome.models

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.data.EnvSensorDisplayedInfo
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRowEntity
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRowEntity.RowNames.*
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingAndDisplayedRows
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingsEntity
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

class EnvSensorDisplayedInfoViewModel(private val localSettingsRepository: LocalSettingsRepository) : ViewModel() {
    val displayedInfo = EnvSensorDisplayedInfo()

    fun updateLiveData(envSensor: EnvSensor) {
        viewModelScope.launch {
            val settingsAndRows: EnvSensorSettingAndDisplayedRows? =
                localSettingsRepository.getEnvSensorSettingsAndDisplayedRows(envSensor.deviceName)

            displayedInfo.apply {
                name.value = envSensor.deviceName
                displayedName.value = settingsAndRows?.envSensorSettings?.displayedName

                if (envSensor.temperature != null) {
                    temperatureOptionVisibility.value = View.VISIBLE
                    isTemperatureDisplayed.value = isRowDisplayed(settingsAndRows?.displayedRows, TEMPERATURE.name)
                }
                if (envSensor.humidity != null) {
                    humidityOptionVisibility.value = View.VISIBLE
                    isHumidityDisplayed.value = isRowDisplayed(settingsAndRows?.displayedRows, HUMIDITY.name)
                }
                if (envSensor.light != null) {
                    lightOptionVisibility.value = View.VISIBLE
                    isLightDisplayed.value = isRowDisplayed(settingsAndRows?.displayedRows, LIGHT.name)
                }
                if (envSensor.gain != null) {
                    gainOptionVisibility.value = View.VISIBLE
                    isGainDisplayed.value = isRowDisplayed(settingsAndRows?.displayedRows, GAIN.name)
                }
                if (envSensor.errors != null) {
                    errorsOptionVisibility.value = View.VISIBLE
                    areErrorsDisplayed.value = isRowDisplayed(settingsAndRows?.displayedRows, ERRORS.name)
                }
                if (envSensor.uptime != null) {
                    uptimeOptionVisibility.value = View.VISIBLE
                    isUptimeDisplayed.value = isRowDisplayed(settingsAndRows?.displayedRows, UPTIME.name)
                }
                if (envSensor.freeHeapSpace != null) {
                    freeHeapSpaceOptionVisibility.value = View.VISIBLE
                    isFreeHeapSpaceDisplayed.value = isRowDisplayed(settingsAndRows?.displayedRows, FREE_HEAP.name)
                }
            }
        }
    }

    fun onOk(onFinish: () -> Unit): EnvSensorDisplayedInfo.Detached {
        val detachedInfo: EnvSensorDisplayedInfo.Detached = displayedInfo.getDetached()
        val deviceName = detachedInfo.name

        GlobalScope.launch {
            val envSensorSettings = EnvSensorSettingsEntity(deviceName, detachedInfo.displayedName)

            localSettingsRepository.insertEnvSensorSettings(envSensorSettings)

            saveRowState(detachedInfo.isTemperatureDisplayed, TEMPERATURE, deviceName)
            saveRowState(detachedInfo.isHumidityDisplayed, HUMIDITY, deviceName)
            saveRowState(detachedInfo.isLightDisplayed, LIGHT, deviceName)
            saveRowState(detachedInfo.isGainDisplayed, GAIN, deviceName)
            saveRowState(detachedInfo.areErrorsDisplayed, ERRORS, deviceName)
            saveRowState(detachedInfo.isUptimeDisplayed, UPTIME, deviceName)
            saveRowState(detachedInfo.isFreeHeapSpaceDisplayed, FREE_HEAP, deviceName)
        }

        onFinish()
        return detachedInfo
    }

    private fun isRowDisplayed(displayedRows: List<EnvSensorDisplayedRowEntity>?, rowName: String): Boolean {
        if (displayedRows == null || displayedRows.isEmpty()) {
            // Not initialized yet
            return true
        }
        return displayedRows.firstOrNull{it.rowName == rowName} != null
    }

    private suspend fun saveRowState(
        isColumnDisplayed: Boolean?,
        rowName: EnvSensorDisplayedRowEntity.RowNames,
        deviceName: String) {

        if (isColumnDisplayed != null) {
            if (isColumnDisplayed == true) {
                val newRow = EnvSensorDisplayedRowEntity(null, rowName.name, deviceName)
                localSettingsRepository.insertEnvSensorDisplayedRow(newRow)
            } else {
                localSettingsRepository.deleteEnvSensorDisplayedRow(rowName.name, deviceName)
            }
        }
    }
}
package ua.dp.hammer.superhome.models

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.adapters.EnvSensorsListAdapter
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.data.EnvSensorDisplayedInfo
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRowEntity
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRowEntity.RowNames.*
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingAndDisplayedRows
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.repositories.web.sensors.EnvSensorsWebRepository

class EnvSensorViewModel : AbstractMonitoringViewModel() {
    val sensors: MutableLiveData<List<EnvSensor>> = MutableLiveData()
    var stopUpdating = false

    private var envSensorsWebRepository: EnvSensorsWebRepository? = null

    lateinit var localSettingsRepository: LocalSettingsRepository

    override fun setServerAddressAndInit(serverAddress: String) {
        if (notInitialized || envSensorsWebRepository?.address != serverAddress) {
            envSensorsWebRepository = EnvSensorsWebRepository(serverAddress)
            init()
        }
    }

    override fun startMonitoring(): Job {
        return viewModelScope.launch {
            var success = false

            while (!success) {
                var response: List<EnvSensor>? = null

                try {
                    response = envSensorsWebRepository?.getEnvSensorsValues()
                } catch (e: Throwable) {}

                if (response == null) {
                    delay(10_000)
                    continue
                }

                applyColumnsVisibility(response)
                sensors.value = response
                success = true
            }

            var oftenAmount = 0

            while (isActive) {
                val requestStartTime = System.currentTimeMillis()
                var response: List<EnvSensor>? = null

                try {
                    response = envSensorsWebRepository?.getAllEnvSensorsDataDeferred()
                } catch (e: Throwable) {}

                val requestEndTime = System.currentTimeMillis()

                if ((requestEndTime - requestStartTime) < 1_000) {
                    // Too often
                    oftenAmount++
                } else {
                    oftenAmount = 0
                }

                if (response == null || oftenAmount >= 3) {
                    delay(10_000)
                    oftenAmount = 0
                    continue
                }

                if (!stopUpdating) {
                    applyColumnsVisibility(response)
                    sensors.value = response
                }
            }
        }
    }

    fun updateVisibility(detachedDisplayedInfo: EnvSensorDisplayedInfo.Detached, adapter: EnvSensorsListAdapter) {
        var envSensorIndex = 0
        var envSensor : EnvSensor? = null

        for (collEnvSensor in sensors.value.orEmpty()) {
            if (collEnvSensor.deviceName == detachedDisplayedInfo.name) {
                envSensor = collEnvSensor
                break
            }
            envSensorIndex++
        }

        if (envSensor == null) {
            return
        }

        viewModelScope.launch {
            envSensor.displayedName.value = detachedDisplayedInfo.displayedName
            envSensor.temperatureVisibility.value = getVisibility(detachedDisplayedInfo.isTemperatureDisplayed)
            envSensor.humidityVisibility.value = getVisibility(detachedDisplayedInfo.isHumidityDisplayed)
            envSensor.lightVisibility.value = getVisibility(detachedDisplayedInfo.isLightDisplayed)
            envSensor.gainVisibility.value = getVisibility(detachedDisplayedInfo.isGainDisplayed)
            envSensor.errorsVisibility.value = getVisibility(detachedDisplayedInfo.areErrorsDisplayed)
            envSensor.uptimeVisibility.value = getVisibility(detachedDisplayedInfo.isUptimeDisplayed)
            envSensor.freeHeapSpaceVisibility.value = getVisibility(detachedDisplayedInfo.isFreeHeapSpaceDisplayed)

            adapter.notifyItemChanged(envSensorIndex)
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
            val displayedNameSettings = settingsAndRows?.envSensorSettings?.displayedName
            val displayedRows = settingsAndRows?.displayedRows

            envSensor.initObservables()

            if (!displayedNameSettings.isNullOrEmpty()) {
                envSensor.displayedName.value = displayedNameSettings
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
                              rowName: EnvSensorDisplayedRowEntity.RowNames,
                              displayedRows: List<EnvSensorDisplayedRowEntity>?): Int {
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
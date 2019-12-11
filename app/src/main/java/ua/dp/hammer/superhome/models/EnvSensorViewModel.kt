package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.repositories.sensors.EnvSensorsRepository

class EnvSensorViewModel : ViewModel() {
    val sensors: MutableLiveData<List<EnvSensor>> = MutableLiveData()
    private val envSensorsRepository: EnvSensorsRepository = EnvSensorsRepository.getInstance()

    init {
        viewModelScope.launch {
            var success = false

            while (!success) {
                try {
                    sensors.value = envSensorsRepository.getEnvSensorsValuesAsync()
                    success = true
                } catch (e: Throwable) {
                    delay(10_000)
                }
            }

            while (isActive) {
                try {
                    val current = System.currentTimeMillis()
                    sensors.value = envSensorsRepository.getAllEnvSensorsDataDeferredAsync()

                    if ((System.currentTimeMillis() - current) < 1000) {
                        delay(10_000)
                    }
                } catch (e: Throwable) {
                    delay(10_000)
                }
            }
        }
    }
}
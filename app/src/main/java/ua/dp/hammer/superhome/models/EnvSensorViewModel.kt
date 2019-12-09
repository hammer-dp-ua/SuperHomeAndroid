package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.repositories.sensors.EnvSensorsRepository

class EnvSensorViewModel() : ViewModel() {
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

            while (true) {
                try {
                    sensors.value = envSensorsRepository.getAllEnvSensorsDataDeferredAsync()
                } catch (e: Throwable) {
                    delay(10_000)
                }
            }
        }
    }
}
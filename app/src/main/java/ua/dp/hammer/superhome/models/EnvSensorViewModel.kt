package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.repositories.sensors.EnvSensorsRepository
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class EnvSensorViewModel(private val envSensorsRepository: EnvSensorsRepository) : ViewModel() {
    val sensors: MutableLiveData<List<EnvSensor>> by lazy {
        loadSensorsLater()
        MutableLiveData<List<EnvSensor>>()
    }

    private fun loadSensorsLater() {
        Executors.newSingleThreadScheduledExecutor().schedule({
            // If you need set a value from a background thread, you can use postValue(Object)
            sensors.postValue(envSensorsRepository.getEnvSensorsValues())
        }, 2, TimeUnit.SECONDS)
    }
}
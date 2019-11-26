package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.repositories.sensors.EnvSensorsRepository

class EnvSensorViewModel(private val envSensorsRepository: EnvSensorsRepository) : ViewModel() {
    val sensors: MutableLiveData<List<EnvSensor>> = MutableLiveData()

    init {
        envSensorsRepository.getEnvSensorsValues(sensors) {
            envSensorsRepository.receiveUpdatedInfoRepeatedly(sensors)
        }
    }
}
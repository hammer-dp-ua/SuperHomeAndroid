package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.repositories.sensors.EnvSensorsRepository

class EnvSensorViewModel(val envSensorsRepository: EnvSensorsRepository) : ViewModel() {
    val sensors: MutableLiveData<List<EnvSensor>> by lazy {
        MutableLiveData<List<EnvSensor>>().also {
            loadSensorsInfo()
        }
    }

    private fun loadSensorsInfo() {
        envSensorsRepository.getEnvSensorsValues()
    }
}
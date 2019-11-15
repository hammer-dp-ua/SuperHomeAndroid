package ua.dp.hammer.superhome.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.dp.hammer.superhome.repositories.sensors.EnvSensorsRepository

class EnvSensorsListFragmentFactory(private val repository: EnvSensorsRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = EnvSensorViewModel(repository) as T
}
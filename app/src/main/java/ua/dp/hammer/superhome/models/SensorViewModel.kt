package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.dp.hammer.superhome.data.EnvSensor

class SensorViewModel : ViewModel() {
    val sensorsInfo: MutableLiveData<List<EnvSensor>> by lazy {
        MutableLiveData<List<EnvSensor>>().also {
            loadSensorsInfo()
        }
    }

    private fun loadSensorsInfo() {
        // Do an asynchronous operation to fetch users.
    }
}
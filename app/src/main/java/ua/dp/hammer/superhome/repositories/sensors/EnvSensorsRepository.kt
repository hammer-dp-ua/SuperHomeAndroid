package ua.dp.hammer.superhome.repositories.sensors

import ua.dp.hammer.superhome.data.EnvSensor

class EnvSensorsRepository private constructor() {
    fun getEnvSensorsValues(): List<EnvSensor> {
        val sensor1 = EnvSensor("temp sensor 1", 25.0F, 99F, 0, -55, null, null, null)
        val sensor2 = EnvSensor("temp sensor 2", -10.0F, 20F, 80, -56, 1, 9844621, 60122)
        val sensor3 = EnvSensor("temp sensor 3", 20.0F, 80F, null, -56, 1, 22554, 60122)
        //val dataToReturn = MutableLiveData<List<EnvSensor>>()

        //dataToReturn.value = listOf(sensor1, sensor2, sensor3)
        return listOf(sensor1, sensor2, sensor3)
    }

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: EnvSensorsRepository? = null

        fun getInstance() : EnvSensorsRepository {
            return instance ?: synchronized(this) {
                instance ?: EnvSensorsRepository().also { instance = it }
            }
        }
    }
}
package ua.dp.hammer.superhome.repositories.sensors

import retrofit2.Call
import retrofit2.http.GET
import ua.dp.hammer.superhome.data.EnvSensor

interface SensorsWebService {
    @GET("info/allSensors")
    fun getAllEnvSensorsData(): Call<List<EnvSensor>>

    @GET("info/deferredSensorsInfo")
    fun getAllEnvSensorsDataDeferred(): Call<List<EnvSensor>>
}
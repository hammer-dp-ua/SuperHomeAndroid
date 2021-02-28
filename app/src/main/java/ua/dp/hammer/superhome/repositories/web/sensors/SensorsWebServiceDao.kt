package ua.dp.hammer.superhome.repositories.web.sensors

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import ua.dp.hammer.superhome.data.EnvSensor

interface SensorsWebServiceDao {
    @GET("info/allSensors")
    fun getAllEnvSensorsDataAsync(): Deferred<List<EnvSensor>>

    @GET("info/deferredSensorsInfo")
    fun getAllEnvSensorsDataDeferredAsync(): Deferred<List<EnvSensor>>
}
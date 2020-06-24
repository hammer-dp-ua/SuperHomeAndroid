package ua.dp.hammer.superhome.repositories.web.sensors

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.dp.hammer.superhome.data.EnvSensor
import ua.dp.hammer.superhome.repositories.CoroutineCallAdapterFactory
import java.util.concurrent.TimeUnit

class EnvSensorsWebRepository(val address: String) {

    private val sensorsWebServiceDao: SensorsWebServiceDao

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(65, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://$address/server/envSensors/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
        sensorsWebServiceDao = retrofit.create(SensorsWebServiceDao::class.java)
    }

    suspend fun getEnvSensorsValues(): List<EnvSensor> {
        return sensorsWebServiceDao.getAllEnvSensorsDataAsync().await()
    }

    suspend fun getAllEnvSensorsDataDeferred(): List<EnvSensor> {
        return sensorsWebServiceDao.getAllEnvSensorsDataDeferredAsync().await()
    }
}
package ua.dp.hammer.superhome.repositories.sensors

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.dp.hammer.superhome.data.EnvSensor
import java.util.concurrent.TimeUnit



class EnvSensorsRepository private constructor() {
    private val sensorsWebServiceDao: SensorsWebServiceDao

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.2/server/envSensors/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
        sensorsWebServiceDao = retrofit.create(SensorsWebServiceDao::class.java)
    }

    suspend fun getEnvSensorsValuesAsync(): List<EnvSensor> {
        return sensorsWebServiceDao.getAllEnvSensorsDataAsync().await()
    }

    suspend fun getAllEnvSensorsDataDeferredAsync(): List<EnvSensor> {
        return sensorsWebServiceDao.getAllEnvSensorsDataDeferredAsync().await()
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
package ua.dp.hammer.superhome.repositories.web.setup

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.dp.hammer.superhome.data.DeviceSetupInfo
import ua.dp.hammer.superhome.repositories.CoroutineCallAdapterFactory
import java.util.concurrent.TimeUnit

class DevicesSetupWebRepository(val address: String) {
    private val devicesSetupWebRepository: DevicesSetupWebServiceDao

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://$address/server/devicesSetup/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
        devicesSetupWebRepository = retrofit.create(DevicesSetupWebServiceDao::class.java)
    }

    suspend fun getAllDevices(): List<DeviceSetupInfo> {
        return devicesSetupWebRepository.getAllDevices().await()
    }

    suspend fun addDevice(device: DeviceSetupInfo): Any {
        return devicesSetupWebRepository.addDevice(device).await()
    }

    suspend fun deleteDevice(deviceName: String): Any {
        return devicesSetupWebRepository.deleteDevice(deviceName).await()
    }
}
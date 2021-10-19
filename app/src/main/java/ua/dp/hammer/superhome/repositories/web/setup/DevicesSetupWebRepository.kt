package ua.dp.hammer.superhome.repositories.web.setup

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.dp.hammer.superhome.repositories.CoroutineCallAdapterFactory
import ua.dp.hammer.superhome.transport.AlarmSourceSetupTransport
import ua.dp.hammer.superhome.transport.DeviceSetupTransport
import ua.dp.hammer.superhome.transport.DeviceTypeSetupTransport
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
            .baseUrl("https://$address/server/setup/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
        devicesSetupWebRepository = retrofit.create(DevicesSetupWebServiceDao::class.java)
    }

    suspend fun getAllDevices(): List<DeviceSetupTransport> {
        return devicesSetupWebRepository.getAllDevices().await()
    }

    suspend fun addDevice(device: DeviceSetupTransport): Any {
        return devicesSetupWebRepository.addDevice(device).await()
    }

    suspend fun deleteDevice(id: Int): Any {
        return devicesSetupWebRepository.deleteDevice(id).await()
    }

    suspend fun getAllDeviceTypes(): List<DeviceTypeSetupTransport> {
        return devicesSetupWebRepository.getAllDeviceTypes().await()
    }

    suspend fun saveDeviceType(deviceType: DeviceTypeSetupTransport): Any {
        return devicesSetupWebRepository.saveDeviceType(deviceType).await()
    }

    suspend fun deleteDeviceType(deviceType: DeviceTypeSetupTransport): Any {
        return devicesSetupWebRepository.deleteDeviceType(deviceType).await()
    }

    suspend fun addAlarmSource(alarmSource: AlarmSourceSetupTransport): Any {
        return devicesSetupWebRepository.addAlarmSource(alarmSource).await()
    }

    suspend fun deleteAlarmSource(aaId: Int): Any {
        return devicesSetupWebRepository.deleteAlarmSource(aaId).await()
    }

    suspend fun modifyAlarmSource(alarmSource: AlarmSourceSetupTransport): Any {
        return devicesSetupWebRepository.modifyAlarmSource(alarmSource).await()
    }

    suspend fun getAlarmSources(): List<AlarmSourceSetupTransport> {
        return devicesSetupWebRepository.getAlarmSources().await()
    }
}
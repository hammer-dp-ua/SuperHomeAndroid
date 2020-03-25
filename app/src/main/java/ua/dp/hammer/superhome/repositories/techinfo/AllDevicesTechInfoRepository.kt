package ua.dp.hammer.superhome.repositories.techinfo

import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.dp.hammer.superhome.data.DeviceTechInfo
import ua.dp.hammer.superhome.data.PhoneAwareDeviceState
import ua.dp.hammer.superhome.repositories.CoroutineCallAdapterFactory
import ua.dp.hammer.superhome.repositories.manager.ManagerRepository
import ua.dp.hammer.superhome.repositories.manager.ManagerWebServiceDao
import java.util.concurrent.TimeUnit

class AllDevicesTechInfoRepository private constructor() {
    private val allDevicesTechInfoWebServiceDao: AllDevicesTechInfoWebServiceDao

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.2/server/devicesTechInfo/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
        allDevicesTechInfoWebServiceDao = retrofit.create(AllDevicesTechInfoWebServiceDao::class.java)
    }

    suspend fun getAllDevicesTechInfoStates(awareStates: Set<PhoneAwareDeviceState>): List<DeviceTechInfo> {
        return allDevicesTechInfoWebServiceDao.getAllDevicesTechInfoStatesAsync(awareStates).await()
    }

    suspend fun getUnavailableDevices(): Set<String> {
        return allDevicesTechInfoWebServiceDao.getUnavailableDevicesAsync().await()
    }

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: AllDevicesTechInfoRepository? = null

        fun getInstance() : AllDevicesTechInfoRepository {
            return instance ?: synchronized(this) {
                instance ?: AllDevicesTechInfoRepository().also { instance = it }
            }
        }
    }
}
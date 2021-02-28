package ua.dp.hammer.superhome.repositories.web.techinfo

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.dp.hammer.superhome.data.DeviceTechInfo
import ua.dp.hammer.superhome.data.PhoneAwareDeviceState
import ua.dp.hammer.superhome.repositories.CoroutineCallAdapterFactory
import java.util.concurrent.TimeUnit

class AllDevicesTechInfoWebRepository(val address: String) {
    private val allDevicesTechInfoWebServiceDao: AllDevicesTechInfoWebServiceDao

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(65, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://$address/server/devicesTechInfo/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
        allDevicesTechInfoWebServiceDao = retrofit.create(AllDevicesTechInfoWebServiceDao::class.java)
    }

    suspend fun getAllDevicesTechInfoStates(awareStates: Set<PhoneAwareDeviceState>): ArrayList<DeviceTechInfo> {
        return allDevicesTechInfoWebServiceDao.getAllDevicesTechInfoStatesAsync(awareStates).await()
    }

    suspend fun getUnavailableDevices(): Set<String> {
        return allDevicesTechInfoWebServiceDao.getUnavailableDevicesAsync().await()
    }
}
package ua.dp.hammer.superhome.repositories.settings

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.dp.hammer.superhome.data.FanSettingsInfo
import ua.dp.hammer.superhome.repositories.CoroutineCallAdapterFactory
import java.util.concurrent.TimeUnit

class ServerSettingsRepository private constructor() {
    private val serverSettingsWebServiceDao: ServerSettingsWebServiceDao

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.2/server/devicesSetup/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
        serverSettingsWebServiceDao = retrofit.create(ServerSettingsWebServiceDao::class.java)
    }

    suspend fun getFanSettings(fanName: String): FanSettingsInfo {
        return serverSettingsWebServiceDao.getFanSettingsAsync(fanName).await()
    }

    suspend fun saveFanSettings(fanSettings: FanSettingsInfo): Any {
        return serverSettingsWebServiceDao.saveFanSettingsAsync(fanSettings).await()
    }

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: ServerSettingsRepository? = null

        fun getInstance() : ServerSettingsRepository {
            return instance ?: synchronized(this) {
                instance ?: ServerSettingsRepository().also { instance = it }
            }
        }
    }
}
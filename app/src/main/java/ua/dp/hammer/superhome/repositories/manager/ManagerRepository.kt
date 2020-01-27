package ua.dp.hammer.superhome.repositories.manager

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.dp.hammer.superhome.data.*
import ua.dp.hammer.superhome.repositories.CoroutineCallAdapterFactory
import java.util.concurrent.TimeUnit

class ManagerRepository private constructor() {
    private val managerWebServiceDao: ManagerWebServiceDao

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.2/server/manager/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
        managerWebServiceDao = retrofit.create(ManagerWebServiceDao::class.java)
    }

    suspend fun stopVideoRecording(timeout: Int): AlarmsState {
        return managerWebServiceDao.stopVideoRecordingAsync(timeout).await()
    }

    suspend fun switchProjectors(state: String): ProjectorState {
        return managerWebServiceDao.switchProjectorsAsync(state).await()
    }

    suspend fun turnOnBathroomFan(): FanState {
        return managerWebServiceDao.turnOnBathroomFanAsync().await()
    }

    suspend fun doShutter(name: String, no: Int, open: Boolean): ShutterState {
        return managerWebServiceDao.doShutter(name, no, open).await()
    }

    suspend fun getCurrentStates(): AllStates {
        return managerWebServiceDao.getCurrentStatesAsync().await()
    }

    suspend fun getCurrentStatesDeferred(): AllStates {
        return managerWebServiceDao.getCurrentStatesDeferredAsync().await()
    }

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: ManagerRepository? = null

        fun getInstance() : ManagerRepository {
            return instance ?: synchronized(this) {
                instance ?: ManagerRepository().also { instance = it }
            }
        }
    }
}
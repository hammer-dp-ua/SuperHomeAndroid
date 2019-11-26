package ua.dp.hammer.superhome.repositories.sensors

import android.util.Log
import androidx.lifecycle.MutableLiveData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.dp.hammer.superhome.data.EnvSensor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit



class EnvSensorsRepository private constructor() {
    private val sensorsWebService: SensorsWebService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.2/server/envSensors/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        sensorsWebService = retrofit.create(SensorsWebService::class.java)
    }

    fun getEnvSensorsValues(sensors: MutableLiveData<List<EnvSensor>>, onSuccess: () -> Unit) {
        sensorsWebService.getAllEnvSensorsData().enqueue(object : Callback<List<EnvSensor>> {
            override fun onResponse(call: Call<List<EnvSensor>>, response: Response<List<EnvSensor>>) {
                if (response.code() == 200) {
                    sensors.postValue(response.body())
                    onSuccess()
                }
            }

            override fun onFailure(call: Call<List<EnvSensor>>, t: Throwable) {
                Log.e(null, "Response error", t)

                // Retry later
                Executors.newSingleThreadScheduledExecutor().schedule({
                    getEnvSensorsValues(sensors, onSuccess)
                }, 30, TimeUnit.SECONDS)
            }
        })
    }

    fun receiveUpdatedInfoRepeatedly(sensors: MutableLiveData<List<EnvSensor>>) {
        sensorsWebService.getAllEnvSensorsDataDeferred().enqueue(object : Callback<List<EnvSensor>> {
            override fun onResponse(call: Call<List<EnvSensor>>, response: Response<List<EnvSensor>>) {
                if (response.code() == 200) {
                    sensors.postValue(response.body())
                    receiveUpdatedInfoRepeatedly(sensors)
                }
            }

            override fun onFailure(call: Call<List<EnvSensor>>, t: Throwable) {
                Log.e(null, "Deferred response error", t)

                // Retry later
                Executors.newSingleThreadScheduledExecutor().schedule({
                    receiveUpdatedInfoRepeatedly(sensors)
                }, 30, TimeUnit.SECONDS)
            }
        })
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
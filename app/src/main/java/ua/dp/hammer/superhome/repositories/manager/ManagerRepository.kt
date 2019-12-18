package ua.dp.hammer.superhome.repositories.manager

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
}
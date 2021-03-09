package ua.dp.hammer.superhome.repositories.web.setup

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ua.dp.hammer.superhome.transport.DeviceSetupTransport

interface DevicesSetupWebServiceDao {
    @GET("allDevices")
    fun getAllDevices(): Deferred<List<DeviceSetupTransport>>

    @POST("addDevice")
    fun addDevice(@Body device: DeviceSetupTransport): Deferred<Any>

    @GET("deleteDevice")
    fun deleteDevice(@Query("id") id: Int): Deferred<Any>
}
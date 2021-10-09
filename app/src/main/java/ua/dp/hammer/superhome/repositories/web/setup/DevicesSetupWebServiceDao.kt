package ua.dp.hammer.superhome.repositories.web.setup

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ua.dp.hammer.superhome.transport.AlarmSourceSetupTransport
import ua.dp.hammer.superhome.transport.DeviceSetupTransport
import ua.dp.hammer.superhome.transport.DeviceTypeSetupTransport

interface DevicesSetupWebServiceDao {
    @GET("allDevices")
    fun getAllDevices(): Deferred<List<DeviceSetupTransport>>

    @POST("addDevice")
    fun addDevice(@Body device: DeviceSetupTransport): Deferred<Any>

    @GET("deleteDevice")
    fun deleteDevice(@Query("id") id: Int): Deferred<Any>

    @POST("saveDeviceType")
    fun saveDeviceType(@Body deviceType: DeviceTypeSetupTransport): Deferred<Any>

    @POST("deleteDeviceType")
    fun deleteDeviceType(@Body deviceType: DeviceTypeSetupTransport): Deferred<Any>

    @GET("getAllDeviceTypes")
    fun getAllDeviceTypes(): Deferred<List<DeviceTypeSetupTransport>>

    @POST("addAlarmSource")
    fun addAlarmSource(@Body alarmSource: AlarmSourceSetupTransport): Deferred<Any>

    @POST("deleteAlarmSource")
    fun deleteAlarmSource(@Body alarmSource: AlarmSourceSetupTransport): Deferred<Any>

    @GET("getAlarmSources")
    fun getAlarmSources(): Deferred<List<AlarmSourceSetupTransport>>
}
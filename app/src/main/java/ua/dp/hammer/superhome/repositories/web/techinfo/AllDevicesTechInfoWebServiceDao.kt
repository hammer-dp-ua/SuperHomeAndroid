package ua.dp.hammer.superhome.repositories.web.techinfo

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ua.dp.hammer.superhome.data.DeviceTechInfo
import ua.dp.hammer.superhome.data.PhoneAwareDeviceState

interface AllDevicesTechInfoWebServiceDao {
    @POST("getAllDevicesTechInfoStates")
    fun getAllDevicesTechInfoStatesAsync(@Body awareStates: Set<PhoneAwareDeviceState>): Deferred<ArrayList<DeviceTechInfo>>

    @GET("getUnavailableDevices")
    fun getUnavailableDevicesAsync(): Deferred<Set<String>>
}
package ua.dp.hammer.superhome.repositories.manager

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import ua.dp.hammer.superhome.data.*

interface ManagerWebServiceDao {
    @GET("ignoreAlarms")
    fun stopVideoRecordingAsync(@Query("timeout") timeout: Int): Deferred<AlarmsState>

    @GET("switchProjectors")
    fun switchProjectorsAsync(@Query("switchState") switchState: String): Deferred<ProjectorState>

    @GET("turnOnBathroomFan")
    fun turnOnBathroomFanAsync(): Deferred<FanState>

    @GET("changeShutterState")
    fun changeShutterStateAsync(@Query("name") name: String,
                                @Query("open") open: Boolean): Deferred<ShutterState>

    @GET("getCurrentStates")
    fun getCurrentStatesAsync(): Deferred<AllStates>

    @GET("getCurrentStatesDeferred")
    fun getCurrentStatesDeferredAsync(): Deferred<AllStates>
}
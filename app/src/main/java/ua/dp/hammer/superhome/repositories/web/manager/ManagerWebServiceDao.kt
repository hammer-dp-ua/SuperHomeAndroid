package ua.dp.hammer.superhome.repositories.web.manager

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

    @GET("shutters")
    fun doShutter(@Query("name") name: String,
                  @Query("no") no: Int,
                  @Query("open") open: Boolean): Deferred<ShutterState>

    @GET("getCurrentStates")
    fun getCurrentStatesAsync(): Deferred<AllStates>

    @GET("getCurrentStatesDeferred")
    fun getCurrentStatesDeferredAsync(): Deferred<AllStates>
}
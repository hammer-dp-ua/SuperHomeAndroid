package ua.dp.hammer.superhome.repositories.manager

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import ua.dp.hammer.superhome.data.AlarmsState
import ua.dp.hammer.superhome.data.AllStates
import ua.dp.hammer.superhome.data.ProjectorState

interface ManagerWebServiceDao {
    @GET("ignoreAlarms")
    fun stopVideoRecordingAsync(@Query("timeout") timeout: Int): Deferred<AlarmsState>

    @GET("switchProjectors")
    fun switchProjectorsAsync(@Query("switchState") switchState: String): Deferred<ProjectorState>

    @GET("turnOnBathroomFun")
    fun turnOnBathroomFanAsync(): Deferred<String>

    @GET("getCurrentStates")
    fun getCurrentStatesAsync(): Deferred<AllStates>

    @GET("getCurrentStatesDeferred")
    fun getCurrentStatesDeferredAsync(): Deferred<AllStates>
}
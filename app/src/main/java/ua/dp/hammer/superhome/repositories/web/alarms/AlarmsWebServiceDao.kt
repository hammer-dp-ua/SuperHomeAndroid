package ua.dp.hammer.superhome.repositories.web.alarms

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface AlarmsWebServiceDao {
    @GET("getCurrentStatesDeferred")
    fun getCurrentStatesDeferred(): Deferred<*>
}
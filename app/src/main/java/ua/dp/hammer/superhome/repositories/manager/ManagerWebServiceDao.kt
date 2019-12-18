package ua.dp.hammer.superhome.repositories.manager

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface ManagerWebServiceDao {
    @GET("ignoreAlarms?timeout={timeout}")
    fun stopVideoRecordingAsync(@Path("timeout") timeout: Int): Deferred<String>

    @GET("switchProjectors?switchState={switchState}")
    fun switchProjectorsAsync(@Path("switchState") switchState: String): Deferred<String>
}
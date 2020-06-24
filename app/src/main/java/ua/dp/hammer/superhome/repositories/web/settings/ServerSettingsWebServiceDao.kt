package ua.dp.hammer.superhome.repositories.web.settings

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ua.dp.hammer.superhome.data.FanSettingsInfo

interface ServerSettingsWebServiceDao {
    @GET("getFanSettings")
    fun getFanSettingsAsync(@Query("name") name: String): Deferred<FanSettingsInfo>

    @POST("saveFanSettings")
    fun saveFanSettingsAsync(@Body fanSettings: FanSettingsInfo): Deferred<Any>
}
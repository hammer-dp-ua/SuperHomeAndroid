package ua.dp.hammer.superhome.repositories.settings

import android.content.Context
import ua.dp.hammer.superhome.db.AppDatabase
import ua.dp.hammer.superhome.db.entities.EnvSensorSettings

class LocalSettingsRepository private constructor(val database: AppDatabase) {
    suspend fun getEnvSensorSettings(name: String): EnvSensorSettings {
        return database.getEnvSensorSettingsDao().getEnvSensorSettings(name)
    }

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: LocalSettingsRepository? = null

        fun getInstance(context: Context) : LocalSettingsRepository {
            return instance ?: synchronized(this) {
                instance ?: LocalSettingsRepository(AppDatabase.getInstance(context)).also { instance = it }
            }
        }
    }
}
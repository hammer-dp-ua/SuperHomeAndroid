package ua.dp.hammer.superhome.repositories.settings

import android.content.Context
import ua.dp.hammer.superhome.db.AppDatabase
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRow
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingAndDisplayedRows
import ua.dp.hammer.superhome.db.entities.EnvSensorSettings

class LocalSettingsRepository private constructor(val database: AppDatabase) {
    suspend fun getEnvSensorSettings(name: String): EnvSensorSettings {
        return database.getEnvSensorSettingsDao().getEnvSensorSettings(name)
    }

    suspend fun getEnvSensorSettingsAndDisplayedRows(name: String): EnvSensorSettingAndDisplayedRows? {
        return database.getEnvSensorSettingsDao().getEnvSensorSettingsAndDisplayedRows(name)
    }

    suspend fun insertEnvSensorSettings(envSensorSettings: EnvSensorSettings) {
        return database.getEnvSensorSettingsDao().insertEnvSensorSettings(envSensorSettings)
    }

    suspend fun insertEnvSensorDisplayedRow(displayedRow: EnvSensorDisplayedRow) {
        return database.getEnvSensorSettingsDao().insertEnvSensorDisplayedRow(displayedRow)
    }

    suspend fun deleteEnvSensorDisplayedRow(rowName: String, ownerSetting: String) {
        return database.getEnvSensorSettingsDao().deleteEnvSensorDisplayedRow(rowName, ownerSetting)
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
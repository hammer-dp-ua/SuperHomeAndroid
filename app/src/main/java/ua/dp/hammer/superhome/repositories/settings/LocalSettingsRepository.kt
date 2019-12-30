package ua.dp.hammer.superhome.repositories.settings

import android.content.Context
import ua.dp.hammer.superhome.db.AppDatabase
import ua.dp.hammer.superhome.db.entities.CameraSettingsEntity
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRowEntity
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingAndDisplayedRows
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingsEntity

class LocalSettingsRepository private constructor(val database: AppDatabase) {
    suspend fun getEnvSensorSettings(name: String): EnvSensorSettingsEntity {
        return database.getEnvSensorSettingsDao().getEnvSensorSettings(name)
    }

    suspend fun getEnvSensorSettingsAndDisplayedRows(name: String): EnvSensorSettingAndDisplayedRows? {
        return database.getEnvSensorSettingsDao().getEnvSensorSettingsAndDisplayedRows(name)
    }

    suspend fun insertEnvSensorSettings(envSensorSettings: EnvSensorSettingsEntity) {
        return database.getEnvSensorSettingsDao().insertEnvSensorSettings(envSensorSettings)
    }

    suspend fun insertEnvSensorDisplayedRow(displayedRowEntity: EnvSensorDisplayedRowEntity) {
        return database.getEnvSensorSettingsDao().insertEnvSensorDisplayedRow(displayedRowEntity)
    }

    suspend fun deleteEnvSensorDisplayedRow(rowName: String, ownerSetting: String) {
        return database.getEnvSensorSettingsDao().deleteEnvSensorDisplayedRow(rowName, ownerSetting)
    }

    suspend fun insertCameraSettings(cameraSettingsEntity: CameraSettingsEntity) {
        val currentSettingsEntity: CameraSettingsEntity? = getCurrentCameraSettings()

        if (currentSettingsEntity == null) {
            database.getManagerSettingsDao().insertCameraSettings(cameraSettingsEntity)
        } else {
            currentSettingsEntity.resumeRecordingHour = cameraSettingsEntity.resumeRecordingHour
            currentSettingsEntity.resumeRecordingMinute = cameraSettingsEntity.resumeRecordingMinute
            database.getManagerSettingsDao().insertCameraSettings(currentSettingsEntity)
        }
    }

    suspend fun getCurrentCameraSettings(): CameraSettingsEntity? {
        return database.getManagerSettingsDao().getCurrentCameraSettings()
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
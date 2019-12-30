package ua.dp.hammer.superhome.db.dao

import androidx.room.*
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRowEntity
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingAndDisplayedRows
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingsEntity

@Dao
interface EnvSensorSettingsDao {
    @Query("SELECT * FROM EnvSensorSettingsEntity WHERE name = :name")
    suspend fun getEnvSensorSettings(name: String): EnvSensorSettingsEntity

    @Transaction
    @Query("SELECT * FROM EnvSensorSettingsEntity WHERE name = :name")
    suspend fun getEnvSensorSettingsAndDisplayedRows(name: String): EnvSensorSettingAndDisplayedRows

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvSensorSettings(envSensorSettings: EnvSensorSettingsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvSensorDisplayedRow(displayedRowEntity: EnvSensorDisplayedRowEntity)

    @Query("DELETE FROM EnvSensorDisplayedRowEntity WHERE rowName = :rowName AND ownerSetting = :ownerSetting")
    suspend fun deleteEnvSensorDisplayedRow(rowName: String, ownerSetting: String)
}
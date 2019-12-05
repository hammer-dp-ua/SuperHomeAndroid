package ua.dp.hammer.superhome.db.dao

import androidx.room.*
import ua.dp.hammer.superhome.db.entities.EnvSensorDisplayedRow
import ua.dp.hammer.superhome.db.entities.EnvSensorSettingAndDisplayedRows
import ua.dp.hammer.superhome.db.entities.EnvSensorSettings

@Dao
interface EnvSensorSettingsDao {
    @Query("SELECT * FROM EnvSensorSettings WHERE name = :name")
    suspend fun getEnvSensorSettings(name: String): EnvSensorSettings

    @Transaction
    @Query("SELECT * FROM EnvSensorSettings WHERE name = :name")
    suspend fun getEnvSensorSettingsAndDisplayedRows(name: String): EnvSensorSettingAndDisplayedRows

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvSensorSettings(envSensorSettings: EnvSensorSettings)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnvSensorDisplayedRow(displayedRow: EnvSensorDisplayedRow)
}
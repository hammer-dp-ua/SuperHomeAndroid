package ua.dp.hammer.superhome.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.dp.hammer.superhome.db.entities.CameraSettingsEntity

@Dao
interface ManagerSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCameraSettings(cameraSettingsEntity: CameraSettingsEntity)

    @Query("SELECT * FROM CameraSettingsEntity")
    suspend fun getCurrentCameraSettings(): CameraSettingsEntity?
}
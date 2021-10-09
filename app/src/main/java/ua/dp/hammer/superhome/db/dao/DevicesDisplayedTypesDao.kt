package ua.dp.hammer.superhome.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.dp.hammer.superhome.db.entities.DeviceDisplayedTypeEntity

@Dao
interface DevicesDisplayedTypesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDeviceDisplayedType(deviceDisplayedType: DeviceDisplayedTypeEntity)

    @Query("SELECT * FROM DeviceDisplayedTypeEntity WHERE type = :type")
    suspend fun getDeviceDisplayedType(type: String): DeviceDisplayedTypeEntity?
}
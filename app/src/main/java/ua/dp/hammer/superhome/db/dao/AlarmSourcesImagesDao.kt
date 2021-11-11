package ua.dp.hammer.superhome.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.dp.hammer.superhome.db.entities.AlarmSourceImageEntity

@Dao
interface AlarmSourcesImagesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAlarmSourceImage(alarmSourceImageEntity: AlarmSourceImageEntity)

    @Query("SELECT * FROM AlarmSourceImageEntity WHERE deviceName = :deviceName and alarmSource = :alarmSource")
    suspend fun getAlarmSourceImage(deviceName: String, alarmSource: String): AlarmSourceImageEntity?
}
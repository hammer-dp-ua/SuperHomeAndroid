package ua.dp.hammer.superhome.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.dp.hammer.superhome.db.entities.LocalSettingsEntity

@Dao
interface LocalSettingsDao {
    @Query("SELECT * FROM LocalSettingsEntity")
    suspend fun getLocalSettings(): LocalSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocalSettings(localSettings: LocalSettingsEntity)
}
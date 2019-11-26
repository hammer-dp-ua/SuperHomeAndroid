package ua.dp.hammer.superhome.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "env_sensor_settings")
class EnvSensorSettings(
    @PrimaryKey @ColumnInfo(name = "name") val name: String,
    val displayedName: String
) {

}
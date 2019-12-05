package ua.dp.hammer.superhome.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EnvSensorSettings(
    @PrimaryKey val name: String,
    val displayedName: String,
    val forPortrait: Boolean
)
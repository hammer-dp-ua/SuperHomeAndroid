package ua.dp.hammer.superhome.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EnvSensorSettingsEntity(
    @PrimaryKey val name: String,
    val displayedName: String?
) {
    override fun toString(): String {
        return EnvSensorSettingsEntity::class.java.simpleName + ". Name: $name, displayed name: $displayedName"
    }
}
package ua.dp.hammer.superhome.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeviceDisplayedTypeEntity(
    @PrimaryKey
    val type: String,
    val displayedType: String?
)
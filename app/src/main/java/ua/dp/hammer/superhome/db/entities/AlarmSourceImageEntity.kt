package ua.dp.hammer.superhome.db.entities

import androidx.room.Entity

@Entity(primaryKeys = ["deviceName", "alarmSource"])
data class AlarmSourceImageEntity(
    val deviceName: String,
    val alarmSource: String,
    val resourceName: String?,
    val filePath: String?
)
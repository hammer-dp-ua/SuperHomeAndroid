package ua.dp.hammer.superhome.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CameraSettingsEntity(
    @PrimaryKey var id: Int?,
    var resumeRecordingHour: Int,
    var resumeRecordingMinute: Int)
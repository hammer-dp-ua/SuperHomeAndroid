package ua.dp.hammer.superhome.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["ownerSetting"])],
    foreignKeys = [ForeignKey(
    entity = EnvSensorSettings::class,
    parentColumns = arrayOf("name"),
    childColumns = arrayOf("ownerSetting"),
    onDelete = CASCADE
)])
data class EnvSensorDisplayedRow(@PrimaryKey(autoGenerate = true) val id: Int?,
                                 val rowName: String,
                                 val ownerSetting: String)
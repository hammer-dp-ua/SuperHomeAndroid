package ua.dp.hammer.superhome.db.entities

import androidx.room.Embedded
import androidx.room.Relation

class EnvSensorSettingAndDisplayedRows {
    @Embedded
    var envSensorSettings: EnvSensorSettings? = null

    @Relation(parentColumn = "name", entityColumn = "ownerSetting")
    var displayedRows: List<EnvSensorDisplayedRow> = ArrayList()
}
package ua.dp.hammer.superhome.db.entities

import androidx.room.Embedded
import androidx.room.Relation

class EnvSensorSettingAndDisplayedRows {
    @Embedded
    var envSensorSettings: EnvSensorSettings? = null

    @Relation(parentColumn = "name", entityColumn = "ownerSetting")
    var displayedRows: List<EnvSensorDisplayedRow> = ArrayList()

    override fun toString(): String {
        val rows: StringBuilder = StringBuilder()

        for (displayedRow in displayedRows) {
            rows.append(displayedRow.toString())
            rows.append("\n")
        }
        return EnvSensorSettingAndDisplayedRows::class.java.simpleName + ":\n$envSensorSettings\n$rows"
    }
}
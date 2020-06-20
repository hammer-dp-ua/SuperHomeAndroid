package ua.dp.hammer.superhome.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class LocalSettingsEntity(
    @PrimaryKey
    var id: Int? = null,
    var localServerAddress: String? = null,
    var globalServerAddress: String? = null
)
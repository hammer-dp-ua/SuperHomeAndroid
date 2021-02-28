package ua.dp.hammer.superhome.db.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class LocalSettingsEntity(
    @PrimaryKey
    var id: Int? = null,
    @Ignore // 192.168.0.2 is set in SSL certificate
    val localServerAddressStatic: String = "192.168.0.2",
    var globalServerAddress: String? = null,
    var localWiFiSsid: String? = null
)
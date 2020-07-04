package ua.dp.hammer.superhome.utilities

import android.content.Context
import android.net.wifi.WifiManager
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

suspend fun getServerAddress(context: Context?, localSettingsRepository: LocalSettingsRepository): String? {
    val wifiManager = context?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val localSettingsEntity = localSettingsRepository.getLocalSettings()

    return if (localSettingsEntity?.localWiFiSsid == "test" ||
        wifiManager.connectionInfo.ssid == "\"" + localSettingsEntity?.localWiFiSsid + "\"") {
        localSettingsEntity?.localServerAddressStatic
    } else {
        localSettingsEntity?.globalServerAddress
    }
}
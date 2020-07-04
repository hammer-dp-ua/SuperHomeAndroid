package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.db.entities.LocalSettingsEntity
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

class MainSettingsViewModel(private val localSettingsRepository: LocalSettingsRepository) : ViewModel() {

    val localServerAddress: MutableLiveData<String> = MutableLiveData()
    val globalServerAddress: MutableLiveData<String> = MutableLiveData()
    val localWiFiSsid: MutableLiveData<String> = MutableLiveData()

    fun loadSettings()  {
        viewModelScope.launch {
            val settings: LocalSettingsEntity? = localSettingsRepository.getLocalSettings()

            if (settings?.localServerAddressStatic != null) {
                localServerAddress.value = settings.localServerAddressStatic
            }
            if (settings?.globalServerAddress != null) {
                globalServerAddress.value = settings.globalServerAddress
            }
            if (settings?.localWiFiSsid != null) {
                localWiFiSsid.value = settings.localWiFiSsid
            }
        }
    }

    fun saveSettings() {
        viewModelScope.launch {
            var settings: LocalSettingsEntity? = localSettingsRepository.getLocalSettings()
            var toSave = false

            if (settings == null) {
                settings = LocalSettingsEntity()
            }

            if (settings.localWiFiSsid != localWiFiSsid.value &&
                localWiFiSsid.value?.isNotEmpty() == true) {
                settings.localWiFiSsid = localWiFiSsid.value
                toSave = true
            }

            if (toSave) {
                localSettingsRepository.saveLocalSettings(settings)
            }
        }
    }
}
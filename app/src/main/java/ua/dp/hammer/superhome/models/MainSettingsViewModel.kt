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

    fun loadSettings()  {
        viewModelScope.launch {
            val settings: LocalSettingsEntity? = localSettingsRepository.getLocalSettings()

            if (settings?.localServerAddress != null) {
                localServerAddress.value = settings.localServerAddress
            }
            if (settings?.globalServerAddress != null) {
                globalServerAddress.value = settings.globalServerAddress
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

            if (settings.localServerAddress != localServerAddress.value) {
                settings.localServerAddress = localServerAddress.value
                toSave = true
            }
            if (settings.globalServerAddress != globalServerAddress.value) {
                settings.globalServerAddress = globalServerAddress.value
                toSave = true
            }

            if (toSave) {
                localSettingsRepository.saveLocalSettings(settings)
            }
        }
    }
}
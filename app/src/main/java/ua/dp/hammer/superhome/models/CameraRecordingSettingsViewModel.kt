package ua.dp.hammer.superhome.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.db.entities.CameraSettingsEntity
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository

class CameraRecordingSettingsViewModel(private val localSettingsRepository: LocalSettingsRepository) : ViewModel() {
    fun saveSettings(pickerHour: Int, pickerMinute: Int) {
        val settings = CameraSettingsEntity(null, pickerHour, pickerMinute)

        viewModelScope.launch {
            localSettingsRepository.insertCameraSettings(settings)
        }
    }
}
package ua.dp.hammer.superhome.models

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.dp.hammer.superhome.data.ImageItem
import ua.dp.hammer.superhome.data.setup.AlarmDisplayedOrderItemObservable
import ua.dp.hammer.superhome.db.entities.AlarmSourceImageEntity
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository
import ua.dp.hammer.superhome.repositories.web.setup.DevicesSetupWebRepository
import ua.dp.hammer.superhome.utilities.getDrawableResourceID

class AlarmsDisplayedOrderViewModel : ViewModel() {
    val alarmsSources: MutableLiveData<Array<AlarmDisplayedOrderItemObservable>> = MutableLiveData()

    private var notInitialized = true
    private lateinit var devicesSetupWebRepository: DevicesSetupWebRepository
    lateinit var localSettingsRepository: LocalSettingsRepository

    fun setServerAddress(serverAddress: String) {
        if (notInitialized || devicesSetupWebRepository.address != serverAddress) {
            devicesSetupWebRepository = DevicesSetupWebRepository(serverAddress)
            notInitialized = false
        }
    }

    fun clearAllAlarmSourcesBeforeLoadingNew() {
        alarmsSources.value = emptyArray()
    }

    suspend fun loadAllAlarmSources(context: Context) {
        val loadedAlarmSources = mutableListOf<AlarmDisplayedOrderItemObservable>()

        for (alarmSource in devicesSetupWebRepository.getAlarmSources()) {
            val drawable = findDrawableImage(context, alarmSource.deviceName, alarmSource.alarmSource)
            loadedAlarmSources.add(AlarmDisplayedOrderItemObservable(alarmSource.deviceName, alarmSource.alarmSource, drawable))
        }

        alarmsSources.value = loadedAlarmSources.toTypedArray()
    }

    private suspend fun findDrawableImage(context: Context, deviceName: String, alarmSource: String): Drawable? {
        val alarmSourceImageEntity = localSettingsRepository.getAlarmSourceImage(deviceName, alarmSource)

        if (alarmSourceImageEntity?.resourceName == null) {
            return null
        }

        val resourceID = getDrawableResourceID(context, alarmSourceImageEntity.resourceName)
        return context.resources.getDrawable(resourceID, null)
    }

    fun swap(sourceIndex: Int, destinationIndex: Int) {
        val sourceItem = alarmsSources.value?.get(sourceIndex)
        val destinationItem = alarmsSources.value?.get(destinationIndex)

        if (sourceItem != null && destinationItem != null) {
            alarmsSources.value?.set(sourceIndex, destinationItem)
            alarmsSources.value?.set(destinationIndex, sourceItem)
        }
    }

    fun saveAlarmImage(imageItem: ImageItem, deviceName: String, alarmSource: String) {
        val entity = AlarmSourceImageEntity(
            deviceName,
            alarmSource,
            imageItem.resourceName,
            imageItem.imageFilePath?.path)

        viewModelScope.launch {
            localSettingsRepository.saveAlarmSourceImage(entity)
        }
    }
}
package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.dp.hammer.superhome.data.MotionDetectorInfo

class AlarmsViewModel : ViewModel() {
    val streetMotionDetectors: MutableLiveData<List<MotionDetectorInfo>> = MutableLiveData()


}
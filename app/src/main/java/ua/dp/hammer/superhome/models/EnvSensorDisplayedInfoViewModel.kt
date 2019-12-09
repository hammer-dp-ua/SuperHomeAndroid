package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.dp.hammer.superhome.data.EnvSensorDisplayedInfo

class EnvSensorDisplayedInfoViewModel : ViewModel() {
    val displayedInfo: MutableLiveData<EnvSensorDisplayedInfo> by lazy {
        MutableLiveData<EnvSensorDisplayedInfo>()
    }

    //val name = MutableLiveData<String>()
    //val displayedName = MutableLiveData<String>()
}
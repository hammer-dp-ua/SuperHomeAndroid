package ua.dp.hammer.superhome.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.dp.hammer.superhome.data.ImageItem

class LocalImagesViewModel : ViewModel() {
    val images: MutableLiveData<List<ImageItem>> = MutableLiveData()


}
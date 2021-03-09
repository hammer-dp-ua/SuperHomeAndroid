package ua.dp.hammer.superhome.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class AbstractMonitoringViewModel : ViewModel() {
    protected var notInitialized = true
        private set

    private var statesJob: Job? = null

    fun startOrResumeMonitoring() {
        if (!notInitialized &&
            (statesJob == null || statesJob?.isCancelled == true)) {
            statesJob = startMonitoring()
        }
    }

    fun stopMonitoring() {
        statesJob?.cancel()
    }

    protected fun init() {
        notInitialized = false
    }

    protected abstract fun startMonitoring(): Job
    abstract fun setServerAddressAndInit(serverAddress: String)
}
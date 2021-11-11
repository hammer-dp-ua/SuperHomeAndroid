package ua.dp.hammer.superhome.data.setup

import android.graphics.drawable.Drawable

class AlarmDisplayedOrderItemObservable(
    val deviceName: String,
    val alarmSource: String,
    val image: Drawable?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlarmDisplayedOrderItemObservable

        if (deviceName != other.deviceName) return false
        if (alarmSource != other.alarmSource) return false

        return true
    }

    override fun hashCode(): Int {
        var result = deviceName.hashCode()
        result = 31 * result + alarmSource.hashCode()
        return result
    }
}
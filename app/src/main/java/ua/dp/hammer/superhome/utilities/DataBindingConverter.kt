package ua.dp.hammer.superhome.utilities

import android.text.TextUtils
import androidx.databinding.InverseMethod

class DataBindingConverter {
    companion object {

        @InverseMethod("convertStringToInteger")
        @JvmStatic
        fun convertIntegerToString(value: String): Int? {
            if (TextUtils.isEmpty(value) || !TextUtils.isDigitsOnly(value)) {
                return null
            }

            return value.toInt()
        }

        @JvmStatic
        fun convertStringToInteger(value: Int?): String {
            return value?.toString() ?: ""
        }

        @JvmStatic
        fun convertSecondsToFormattedTime(uptime: Int?, maxDisplayedSectionsParam: Int): String? {
            if (uptime == null) {
                return null
            }

            var secondsRemaining = uptime
            val uptimeDays: Int = secondsRemaining / 60 / 60 / 24

            if (uptimeDays > 0) {
                secondsRemaining -= 60 * 60 * 24 * uptimeDays
            }
            val uptimeHours = secondsRemaining / 60 / 60
            if (uptimeHours > 0) {
                secondsRemaining -= 60 * 60 * uptimeHours
            }
            val uptimeMinutes = secondsRemaining / 60
            if (uptimeMinutes > 0) {
                secondsRemaining -= 60 * uptimeMinutes
            }
            val uptimeSeconds = secondsRemaining

            var prevMatch = false
            var maxDisplayedSections = maxDisplayedSectionsParam
            val result = StringBuilder()

            if (uptimeDays > 0) {
                prevMatch = true
                maxDisplayedSections--
                result.append("${uptimeDays}d")
            }
            if (maxDisplayedSections > 0 && (uptimeHours > 0 || prevMatch)) {
                prevMatch = true
                maxDisplayedSections--
                result.append("${uptimeHours}h")
            }
            if (maxDisplayedSections > 0 && (uptimeMinutes > 0 || prevMatch)) {
                prevMatch = true
                maxDisplayedSections--
                result.append("${uptimeMinutes}m")
            }
            if (maxDisplayedSections > 0 && (uptimeSeconds > 0 || prevMatch)) {
                result.append("${uptimeSeconds}s")
            }
            return result.toString()
        }
    }
}
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
    }
}
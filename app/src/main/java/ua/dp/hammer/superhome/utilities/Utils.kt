package ua.dp.hammer.superhome.utilities

import android.content.Context
import android.os.Environment
import android.os.Looper

// Checks if a volume containing external storage is available to at least read.
fun isExternalStorageReadable(): Boolean {
    return Environment.getExternalStorageState() in
            setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
}

fun isThisMainUIThread(): Boolean {
    return Thread.currentThread() == Looper.getMainLooper().thread
}

fun notNullContext(context: Context?): Context {
    return context ?: throw IllegalStateException("Can't be null")
}

fun getDrawableResourceID(context: Context, resourceName: String): Int {
    return context.resources.getIdentifier(resourceName, "drawable", "ua.dp.hammer.superhome")
}
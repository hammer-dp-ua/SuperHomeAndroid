package ua.dp.hammer.superhome.utilities

import android.os.Environment

// Checks if a volume containing external storage is available to at least read.
fun isExternalStorageReadable(): Boolean {
    return Environment.getExternalStorageState() in
            setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
}
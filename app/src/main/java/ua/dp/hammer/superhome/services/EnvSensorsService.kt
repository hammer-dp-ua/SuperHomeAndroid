package ua.dp.hammer.superhome.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

// A service runs in the main thread.
// Consider using AsyncTask or HandlerThread instead of the traditional Thread class
class EnvSensorsService : Service() {

    override fun onCreate() {

    }

    override fun onStartCommand(intent: Intent?,
                                flags: Int,
                                startId: Int): Int {

        return START_STICKY // If we get killed, after returning from here, restart
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {

    }
}
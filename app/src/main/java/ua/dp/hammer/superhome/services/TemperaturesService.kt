package ua.dp.hammer.superhome.services

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder

class TemperaturesService : Service() {

    override fun onCreate() {

    }

    override fun onStartCommand(intent: Intent?,
                                flags: Int,
                                startId: Int): Int {

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {

    }
}
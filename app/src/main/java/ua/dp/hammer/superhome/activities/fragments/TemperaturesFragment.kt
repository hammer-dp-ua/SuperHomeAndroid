package ua.dp.hammer.superhome.activities.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.services.TemperaturesService

class TemperaturesFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Intent(this, TemperaturesService::class.java).also { intent ->
            startService(intent)
        }
    }
}
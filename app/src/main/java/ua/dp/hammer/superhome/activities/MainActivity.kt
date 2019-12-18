package ua.dp.hammer.superhome.activities

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ua.dp.hammer.superhome.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // to perform relatively CPU-intensive shutdown operations
    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()

        val resolutionView = findViewById<View>(R.id.resolution) as TextView
        val displayMetrics = DisplayMetrics()

        windowManager.defaultDisplay.getMetrics(displayMetrics)
        resolutionView.text = "Height: ${displayMetrics.heightPixels}, Width: ${displayMetrics.widthPixels}"
    }

    override fun onResume() {
        super.onResume()
    }

    // execution is very brief
    override fun onPause() {
        super.onPause()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

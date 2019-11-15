package ua.dp.hammer.superhome.activities

import android.os.Bundle
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

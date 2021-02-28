package ua.dp.hammer.superhome.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.databinding.ActivityMainAppBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (baseContext.resources.getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

            binding.settingsButton?.setOnClickListener {
                val settingsActivityIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsActivityIntent)
            }
        } else {
            val navController = findNavController(R.id.nav_host_fragment)
            findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(navController)
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (!baseContext.resources.getBoolean(R.bool.isTablet)) {
            menuInflater.inflate(R.menu.main_buttom_menu, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!baseContext.resources.getBoolean(R.bool.isTablet)) {
            val navController = findNavController(R.id.nav_graph)
            return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}

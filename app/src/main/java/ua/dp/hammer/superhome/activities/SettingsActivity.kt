package ua.dp.hammer.superhome.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.activities.fragments.MainSettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.settings_fragment_container, MainSettingsFragment::class.java, intent.extras)
        fragmentTransaction.commit()
    }
}
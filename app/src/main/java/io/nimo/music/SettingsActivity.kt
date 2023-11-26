package io.nimo.music

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        val checkBoxPlaySounds: CheckBox = findViewById(R.id.checkBoxPlaySounds)

        //TODO: Add notes sounds to the app - to help train the ear
        checkBoxPlaySounds.isEnabled = false
        val checkBoxHints: CheckBox = findViewById(R.id.checkBoxHints)
        checkBoxPlaySounds.isChecked = sharedPref.getBoolean("PlaySounds", false)
        checkBoxHints.isChecked = sharedPref.getBoolean("ShowHints", false)
        checkBoxHints.setOnCheckedChangeListener { _, _ ->
            with(sharedPref.edit()) {
                putBoolean("ShowHints", checkBoxHints.isChecked)
                apply()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_staff_activity -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                true
            }

            R.id.menu_fret_activity -> {
                val intent = Intent(this, FretBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                true
            }

            R.id.menu_settings_activity -> {
                val intent = Intent(this, SettingsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

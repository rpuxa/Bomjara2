package ru.rpuxa.bomjara.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.settings.Settings
import ru.rpuxa.bomjara.settings.saveSettings

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(settings_toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        show_tips.isChecked = Settings.showTips
        show_tips.setOnCheckedChangeListener { _, isChecked ->
            Settings.showTips = isChecked
        }
    }

    override fun onPause() {
        saveSettings(filesDir)
        super.onPause()
    }

    override fun onDestroy() {
        saveSettings(filesDir)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

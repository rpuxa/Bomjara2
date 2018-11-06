package ru.rpuxa.bomjara.impl.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.impl.Data.settings

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(settings_toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        show_tips.isChecked = settings.showTips
        show_tips.setOnCheckedChangeListener { _, isChecked ->
            settings.showTips = isChecked
        }
        show_vk_invite.isChecked = settings.showVkGroupInvite
        show_vk_invite.setOnCheckedChangeListener { _, isChecked ->
            settings.showVkGroupInvite = isChecked
        }
    }

    override fun onPause() {
        settings.saveToFile(filesDir)
        super.onPause()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_in, R.anim.rigth_out)
    }
}
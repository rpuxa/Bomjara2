package ru.rpuxa.bomjara.impl.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.main_menu.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.impl.fragments.TipFragment
import ru.rpuxa.bomjara.save.SaveLoader
import ru.rpuxa.bomjara.settings.saveSettings
import ru.rpuxa.bomjara.impl.startActivity
import ru.rpuxa.bomjara.impl.startActivityFromRight
import ru.rpuxa.bomjara.impl.views.RateDialog

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this, getString(R.string.ad_id))
        TipFragment.allTips.clear()
        if (intent?.extras?.get(RateDialog.RATE_DIALOG) as? Boolean == true) {
            RateDialog().show(fragmentManager, "rate")
        }
        setContentView(R.layout.main_menu)
        saves.setOnClickListener {
            startActivityFromRight<SavesActivity>()
        }

        quit.setOnClickListener {
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
        }

        menu_settings.setOnClickListener {
            startActivityFromRight<SettingsActivity>()
        }
    }

    override fun onBackPressed() {
    }

    override fun onResume() {
        val save = SaveLoader.findSaveById(ru.rpuxa.bomjara.settings.settings.lastSave)
        if (save == null) {
            continue_game.text = "Новая игра"
            continue_game.setOnClickListener {
                val i = Intent(this, SavesActivity::class.java)
                i.putExtra("new", true)
                startActivity(i)
                overridePendingTransition(R.anim.right_in, R.anim.left_out)
            }
        } else {
            continue_game.text = "Продолжить игру"
            continue_game.setOnClickListener {
                Data.player = Player.fromSave(save)
                startActivity<ContentActivity>()
            }
        }
        super.onResume()
    }

    override fun onPause() {
        saveSettings(filesDir)
        super.onPause()
    }
}

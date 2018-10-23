package ru.rpuxa.bomjara.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.main_menu.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.save.SaveLoader
import ru.rpuxa.bomjara.settings.saveSettings
import ru.rpuxa.bomjara.settings.settings
import ru.rpuxa.bomjara.startActivity
import ru.rpuxa.bomjara.startActivityFromRight
import ru.rpuxa.bomjara.views.RateDialog

class MenuActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TipFragment.allTips.clear()
        SaveLoader.load(filesDir)
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

        rate.setOnClickListener {
            RateDialog().show(fragmentManager, RateDialog.FROM_SETTINGS)
        }
    }

    override fun onBackPressed() {
    }

    override fun onResume() {
        rate.visibility = if (settings.wasRated) View.INVISIBLE else View.VISIBLE
        val save = SaveLoader.findSaveById(ru.rpuxa.bomjara.settings.settings.lastSave)
        if (save == null) {
            continue_game.text = "Новая игра"
            continue_game.setOnClickListener {
                val i = Intent(this, SavesActivity::class.java).apply { putExtra("new", true) }
                startActivity(i)
            }
        } else {
            continue_game.text = "Продолжить игру"
            continue_game.setOnClickListener {
                Player.CURRENT = Player.fromSave(save)
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

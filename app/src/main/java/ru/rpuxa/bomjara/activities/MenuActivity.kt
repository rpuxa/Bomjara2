package ru.rpuxa.bomjara.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_menu.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.save.SaveLoader
import ru.rpuxa.bomjara.settings.Settings
import ru.rpuxa.bomjara.settings.saveSettings
import ru.rpuxa.bomjara.startActivity
import ru.rpuxa.bomjara.startActivityFromRight

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

        settings.setOnClickListener {
            startActivityFromRight<SettingsActivity>()
        }

        rate.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.google_play_link))))
        }
    }

    override fun onBackPressed() {
    }

    override fun onResume() {
        val save = SaveLoader.findSaveById(Settings.lastSave)
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

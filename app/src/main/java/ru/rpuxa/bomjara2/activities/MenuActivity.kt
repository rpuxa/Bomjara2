package ru.rpuxa.bomjara2.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_menu.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.save.SaveLoader
import ru.rpuxa.bomjara2.settings.Settings
import ru.rpuxa.bomjara2.settings.saveSettings
import ru.rpuxa.bomjara2.startActivity

class MenuActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SaveLoader.load(filesDir)
        setContentView(R.layout.main_menu)
        saves.setOnClickListener {
            startActivity<SavesActivity>()
        }

        quit.setOnClickListener {
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
        }

        settings.setOnClickListener {
            startActivity<SettingsActivity>()
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

package ru.rpuxa.bomjara.impl.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.main_menu.*
import org.jetbrains.anko.startActivity
import ru.rpuxa.bomjara.BuildConfig
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.impl.Data.player
import ru.rpuxa.bomjara.impl.Data.saveLoader
import ru.rpuxa.bomjara.impl.Data.settings
import ru.rpuxa.bomjara.impl.fragments.TipFragment
import ru.rpuxa.bomjara.impl.player.PlayerFromSave
import ru.rpuxa.bomjara.impl.views.RateDialog
import ru.rpuxa.bomjara.utils.startActivityFromRight

class MenuActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
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

        menu_version.text = "version ${BuildConfig.VERSION_NAME}"
    }

    override fun onBackPressed() {
    }

    override fun onResume() {
        val save = saveLoader.findSaveById(settings.lastSave)
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
                player = PlayerFromSave(save)
                startActivity<ContentActivity>()
            }
        }
        super.onResume()
    }

    override fun onPause() {
        settings.saveToFile(filesDir)
        super.onPause()
    }
}

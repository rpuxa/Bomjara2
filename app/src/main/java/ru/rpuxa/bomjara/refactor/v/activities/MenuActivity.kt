package ru.rpuxa.bomjara.refactor.v.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_menu.*
import org.jetbrains.anko.startActivity
import ru.rpuxa.bomjara.BuildConfig
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.refactor.vm.SettingsViewModel
import ru.rpuxa.bomjara.utils.getViewModel
import ru.rpuxa.bomjara.utils.startActivityFromRight

class MenuActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val viewModel = getViewModel<SettingsViewModel>()
        if (viewModel.isSavesIsEmpty()) {
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
                startActivity<ContentActivity>()
            }
        }
        super.onResume()
    }
}

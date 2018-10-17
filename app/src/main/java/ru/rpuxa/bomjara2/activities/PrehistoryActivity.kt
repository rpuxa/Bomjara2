package ru.rpuxa.bomjara2.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_prehistory.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.settings.Settings
import ru.rpuxa.bomjara2.startActivity

class PrehistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prehistory)
        val radio1 = history1_radiobutton
        val radio2 = history2_radiobutton
        val radio3 = history3_radiobutton

        history1.setOnClickListener {
            radio1.isChecked = true
            radio2.isChecked = false
            radio3.isChecked = false
            start_survive.visibility = View.VISIBLE
        }

        history2.setOnClickListener {
            radio1.isChecked = false
            radio2.isChecked = true
            radio3.isChecked = false
            start_survive.visibility = View.VISIBLE
        }

        history3.setOnClickListener {
            radio1.isChecked = false
            radio2.isChecked = false
            radio3.isChecked = true
            start_survive.visibility = View.VISIBLE
        }

        start_survive.setOnClickListener {
            when {
                radio1.isChecked -> {
                    Player.CURRENT.money.rubles = 500
                }
                radio2.isChecked -> {
                    Player.CURRENT.maxCondition.health = 120
                }
                radio3.isChecked -> {
                    Player.CURRENT.possessions.transport = 1
                    Player.CURRENT.courses[0] = Actions.COURSES[0].length
                }
            }
            Player.CURRENT.age++
            startActivity<ContentActivity>()
            Settings.lastSave = Player.CURRENT.id

        }
    }

    override fun onBackPressed() {
    }
}

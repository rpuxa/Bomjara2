package ru.rpuxa.bomjara.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_prehistory.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.settings.settings
import ru.rpuxa.bomjara.startActivity

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
                    Player.current.money.rubles = 500
                }
                radio2.isChecked -> {
                    Player.current.maxCondition.health = 120
                }
                radio3.isChecked -> {
                    Player.current.possessions.transport = 1
                    Player.current.courses[0] = Actions.COURSES[0].length
                }
            }
            Player.current.age++
            startActivity<ContentActivity>()
            settings.lastSave = Player.current.id

        }
    }

    override fun onBackPressed() {
    }
}

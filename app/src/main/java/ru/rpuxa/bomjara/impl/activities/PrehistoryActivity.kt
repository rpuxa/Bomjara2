package ru.rpuxa.bomjara.impl.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_prehistory.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.impl.actions.Actions
import ru.rpuxa.bomjara.settings.settings
import ru.rpuxa.bomjara.impl.startActivity

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
                    Data.player.money.rubles = 500
                }
                radio2.isChecked -> {
                    Data.player.maxCondition.health = 120
                }
                radio3.isChecked -> {
                    Data.player.possessions.transport = 1
                    Data.player.courses[0] = Actions.courses[0].length
                }
            }
            Data.player.age++
            startActivity<ContentActivity>()
            settings.lastSave = Data.player.id

        }
    }

    override fun onBackPressed() {
    }
}

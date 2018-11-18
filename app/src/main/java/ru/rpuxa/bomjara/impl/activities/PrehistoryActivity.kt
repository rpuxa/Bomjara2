package ru.rpuxa.bomjara.impl.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_prehistory.*
import org.jetbrains.anko.startActivity
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.impl.Data.actionsBase
import ru.rpuxa.bomjara.impl.Data.player
import ru.rpuxa.bomjara.impl.Data.settings

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
                    player.money.rubles = 500
                }
                radio2.isChecked -> {
                    player.maxCondition.health = 120
                }
                radio3.isChecked -> {
                    player.possessions.transport = 1
                    player.courses[0] = actionsBase.courses[0].length
                }
            }
            player.age++
            startActivity<ContentActivity>()
            settings.lastSave = player.id

        }
    }

    override fun onBackPressed() {
    }
}

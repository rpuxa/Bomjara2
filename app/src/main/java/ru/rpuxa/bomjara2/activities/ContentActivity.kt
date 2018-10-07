package ru.rpuxa.bomjara2.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.status_bars.*
import kotlinx.android.synthetic.main.status_bars.view.*
import ru.rpuxa.bomjara2.ContentAdapter
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.R.drawable
import ru.rpuxa.bomjara2.divider
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.game.player.Condition
import ru.rpuxa.bomjara2.game.player.Money

class ContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content)
        Player.CURRENT.listener = PlayerListener()

        pager.adapter = ContentAdapter(supportFragmentManager)
        scroll_buttons
                .setIcons(drawable.energy, drawable.food, drawable.health, drawable.job)
                .setColoredIcons(drawable.colored_energy, drawable.colored_food, drawable.colored_health, drawable.colored_job)
                .setViewPager(pager)
    }

    override fun onDestroy() {
        Player.CURRENT.listener = null
        super.onDestroy()
    }


    inner class PlayerListener : Player.Listener {

        override fun onCaughtByPolice() {
        }

        override fun onMoneyChanged(money: Money, player: Player, positive: Boolean) {
            status_bars.rubles.text = money.rubles.divider()
            status_bars.euros.text = money.euros.divider()
            status_bars.bitcoins.text = money.bitcoins.divider()
        }

        override fun onConditionChanged(condition: Condition, player: Player, maxCondition: Condition) {
            energy_bar.progress = condition.energy
            energy_bar.max = maxCondition.energy

            fullness_bar.progress = condition.fullness
            fullness_bar.max = maxCondition.fullness

            health_bar.progress = condition.health
            health_bar.max = condition.health
        }

        override fun onDead() {
        }
    }

}
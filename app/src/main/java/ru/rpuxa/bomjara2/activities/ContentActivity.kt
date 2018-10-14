package ru.rpuxa.bomjara2.activities

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.status_bars.*
import kotlinx.android.synthetic.main.status_bars.view.*
import ru.rpuxa.bomjara2.*
import ru.rpuxa.bomjara2.R.drawable
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.game.player.Condition
import ru.rpuxa.bomjara2.game.player.Money

class ContentActivity : AppCompatActivity() {

    private lateinit var deadBanner: Ad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content)
        Player.CURRENT.listener = PlayerListener()

        pager.adapter = ContentAdapter(supportFragmentManager)
        scroll_buttons
                .setIcons(
                        drawable.info,
                        drawable.currencies,
                        drawable.location,
                        drawable.transport,
                        drawable.courses,
                        drawable.energy,
                        drawable.food,
                        drawable.health,
                        drawable.job,
                        drawable.vip
                )
                .setColoredIcons(
                        drawable.colored_info,
                        drawable.colored_currencies,
                        drawable.colored_location,
                        drawable.colored_transport,
                        drawable.colored_courses,
                        drawable.colored_energy,
                        drawable.colored_food,
                        drawable.colored_health,
                        drawable.colored_job,
                        drawable.colored_vip
                )
                .setViewPager(pager)


        bottom_banner.adSize = AdSize.BANNER
        bottom_banner.adUnitId = getString(R.string.bottom_banner_id)
        bottom_banner.loadAd(AdRequest.Builder().build())

        deadBanner = Ad(this, getString(R.string.dead_banner_id))
    }

    override fun onResume() {
        deadBanner.resume()
        super.onResume()
    }

    override fun onPause() {
        deadBanner.pause()
        super.onPause()
    }

    override fun onDestroy() {
        Player.CURRENT.listener = null
        deadBanner.destroy()
        super.onDestroy()
    }


    inner class PlayerListener : Player.Listener {

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
            health_bar.max = maxCondition.health
        }

        override fun onDead(player: Player, hunger: Boolean) {
            super.onDead(player, hunger)
            var dialog = null as Dialog?
            dialog = AlertDialog.Builder(this@ContentActivity)
                    .setTitle("Бомж умер от " + if (hunger) "голода" else "болезни")
                    .setMessage(
                            (if (hunger) "Уровень сытости опустился ниже нуля!" else "Уровень здоровья опустился ниже нуля!") +
                                    "\nВы можете воскресить его, посмотрев рекламу,\n" +
                                    "или начать заново, потеряв прогресс"
                    )
                    .setCancelable(false)
                    .setNegativeButton("Начать заново") { _, _ ->
                        TODO()
                    }
                    .setPositiveButton("Воскресить!") { _, _ ->
                        val res = deadBanner.show {
                            toast("Мы вас с того света достали! Пожалуйста, не забывайте о своем здоровье", false)
                            player.deadByZeroHealth = false
                            player.deadByHungry = false
                            player.condition.fullness = player.maxCondition.fullness
                            player.condition.health = player.maxCondition.health
                            dialog!!.dismiss()
                        }
                        if (!res) {
                            toast("Загрузка... Пожалуйста подождите")
                        }
                    }
                    .show()
        }

        override fun onCaughtByPolice(player: Player) {
            super.onCaughtByPolice(player)
            var dialog = null as Dialog?
            dialog = AlertDialog.Builder(this@ContentActivity)
                    .setTitle("Вас поймали менты!")
                    .setMessage(
                            "Вы можете посмотреть рекламу, чтобы вас отпустили\n" +
                                    "или заплатить штраф"
                    )
                    .setCancelable(false)
                    .setNegativeButton("Заплатить штраф") { _, _ ->
                        TODO()
                    }
                    .setPositiveButton("Не платить штраф") { _, _ ->
                        val res = deadBanner.show {
                            toast("Так уж и быть мы вас отпустим. Впреть будьте аккуратнее", false)
                            player.caughtByPolice = false
                            player.condition.fullness = player.maxCondition.fullness
                            player.condition.health = player.maxCondition.health
                            dialog!!.dismiss()
                        }
                        if (!res) {
                            toast("Загрузка... Пожалуйста подождите")
                        }
                    }
                    .show()
        }
    }

}
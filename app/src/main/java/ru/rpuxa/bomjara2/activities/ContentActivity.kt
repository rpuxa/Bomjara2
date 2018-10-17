package ru.rpuxa.bomjara2.activities

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.status_bars.*
import kotlinx.android.synthetic.main.status_bars.view.*
import ru.rpuxa.bomjara2.*
import ru.rpuxa.bomjara2.R.drawable
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.game.CurrencyExchange
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


        bottom_banner.loadAd(AdRequest.Builder().build())
        bottom_banner.adListener = object : AdListener() {
            override fun onAdClicked() {
                bottom_banner.visibility = View.GONE
            }
        }

        deadBanner = Ad(this, getString(R.string.dead_banner_id))

        save()
    }

    override fun onResume() {
        deadBanner.resume()
        super.onResume()
    }

    override fun onPause() {
        save()
        deadBanner.pause()
        super.onPause()
    }

    override fun onDestroy() {
        save()
        Player.CURRENT.listener = null
        deadBanner.destroy()
        super.onDestroy()
    }

    fun gotoMainMenu() {
        startActivity<MenuActivity>()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setTitle("Выйти в главное меню?")
                .setPositiveButton("Выйти") { _, _ ->
                    gotoMainMenu()
                }
                .setNegativeButton("Отмена", null)
                .show()
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
            val dialog = AlertDialog.Builder(this@ContentActivity)
                    .setTitle("Бомж умер от " + if (hunger) "голода" else "болезни")
                    .setMessage(
                            (if (hunger) "Уровень сытости опустился ниже нуля!" else "Уровень здоровья опустился ниже нуля!") +
                                    " Вы можете воскресить его, посмотрев рекламу, " +
                                    "или начать заново, потеряв прогресс"
                    )
                    .setIcon(R.drawable.dead)
                    .setCancelable(false)
                    .setNegativeButton("Начать заново") { _, _ ->
                        gotoMainMenu()
                    }
                    .setPositiveButton("Воскресить!", null)
                    .show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val res = deadBanner.show {
                    toast("Мы вас с того света достали! Пожалуйста, не забывайте о своем здоровье", false)
                    player.deadByZeroHealth = false
                    player.deadByHungry = false
                    player.condition.fullness = player.maxCondition.fullness
                    player.condition.health = player.maxCondition.health
                    dialog.dismiss()
                }
                if (!res) {
                    toast("Загрузка... Пожалуйста подождите")
                }
            }

        }

        override fun onCaughtByPolice(player: Player) {
            super.onCaughtByPolice(player)
            val dialog = AlertDialog.Builder(this@ContentActivity)
                    .setTitle("Вас поймали менты!")
                    .setMessage(
                            "Вы можете посмотреть рекламу, чтобы вас отпустили" +
                                    " или заплатить штраф в размере ${Actions.penalty} рублей"
                    )
                    .setCancelable(false)
                    .setIcon(R.drawable.prison)
                    .setNegativeButton("Заплатить штраф", null)
                    .setPositiveButton("Не платить штраф", null)
                    .show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val res = deadBanner.show {
                    toast("Так уж и быть мы вас отпустим. Впредь будьте аккуратнее", false)
                    player.caughtByPolice = false
                    player.condition.fullness = player.maxCondition.fullness
                    player.condition.health = player.maxCondition.health
                    dialog.dismiss()
                }
                if (!res) {
                    toast("Загрузка... Пожалуйста подождите")
                }
            }

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener penalty@{
                if (Player.CURRENT.add((-Actions.penalty).rub)) {
                    dialog.dismiss()
                    return@penalty
                }
                Player.CURRENT.add(Money(rubles = CurrencyExchange.convert(Player.CURRENT.money.euros, EURO, RUB, false)))
                Player.CURRENT.money.euros = 0
                if (Player.CURRENT.add((-Actions.penalty).rub)) {
                    toast("Для выплаты штрафа все ваши евро были переведены в рубли", false)
                    dialog.dismiss()
                    return@penalty
                }

                Player.CURRENT.add(Money(rubles = CurrencyExchange.convert(Player.CURRENT.money.bitcoins, BITCOIN, RUB, false)))
                Player.CURRENT.money.bitcoins = 0
                if (Player.CURRENT.add((-Actions.penalty).rub)) {
                    toast("Для выплаты штрафа все ваши евро и биткоины были переведены в рубли", false)
                    dialog.dismiss()
                    return@penalty
                }
                toast(getString(R.string.money_needed))
            }
        }
    }

}
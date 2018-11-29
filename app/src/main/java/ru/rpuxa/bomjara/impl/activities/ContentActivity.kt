package ru.rpuxa.bomjara.impl.activities

import android.animation.ValueAnimator
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.status_bars.*
import kotlinx.android.synthetic.main.status_bars.view.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import ru.rpuxa.bomjara.CurrentData
import ru.rpuxa.bomjara.CurrentData.player
import ru.rpuxa.bomjara.CurrentData.statistic
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.R.drawable.*
import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.impl.ContentAdapter
import ru.rpuxa.bomjara.impl.player.of
import ru.rpuxa.bomjara.impl.player.rub
import ru.rpuxa.bomjara.impl.views.RateDialog
import ru.rpuxa.bomjara.utils.divider
import ru.rpuxa.bomjara.utils.save


class ContentActivity : AppCompatActivity() {

    val ad get() = (application as App).videoAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content)
        statistic.loadPlayer(player)
        player.listener = PlayerListener()

        pager.adapter = ContentAdapter(supportFragmentManager)
        scroll_buttons
                .setIcons(
                        info,
                        currencies,
                        location,
                        transport,
                        courses,
                        energy,
                        food,
                        health,
                        job,
                        vip
                )
                .setColoredIcons(
                        colored_info,
                        colored_currencies,
                        colored_location,
                        colored_transport,
                        colored_courses,
                        colored_energy,
                        colored_food,
                        colored_health,
                        colored_job,
                        colored_vip
                )
                .setViewPager(pager)


//        bottom_banner.loadAd(AdRequest.Builder().build())


        //DEBUG
        /*  if (BuildConfig.DEBUG) {
              player.money.rubles = 999999999
              player.money.euros = 999999999
              player.money.bitcoins = 999999999
              player.money.bottles = 999999999
              player.possessions.home = 7
              player.possessions.transport = 7
          }
  */

        CurrentData.statistic.sendStatistic()
        save()
    }

    override fun onPause() {
        save()
        super.onPause()
    }

    override fun onDestroy() {
        save()
        player.listener = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.exit_to_main_menu))
                .setPositiveButton(getString(R.string.exit)) { _, _ ->
                    startActivity<MenuActivity>()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
    }

    private inner class PlayerListener : Player.Listener {

        private var anim = ValueAnimator.ofFloat()!!

        override fun onMoneyChanged(player: Player, positive: Boolean, currency: Int, addCount: Long) {
            fun set(textView: TextView, count: Long, currency0: Int) {
                textView.text = count.divider()
                if (currency != currency0 || addCount == 0L)
                    return
                val startColor = if (positive) Color.GREEN else Color.RED
                val toColor = Color.WHITE
                anim.cancel()
                anim = ValueAnimator.ofFloat(1f, 0f)
                anim.duration = 1000
                anim.addUpdateListener {
                    val v = it.animatedValue as Float
                    val nv = 1 - v
                    val r = (Color.red(startColor) * v + nv * Color.red(toColor)).toInt()
                    val g = (Color.green(startColor) * v + nv * Color.green(toColor)).toInt()
                    val b = (Color.blue(startColor) * v + nv * Color.blue(toColor)).toInt()

                    textView.setTextColor(Color.argb(255, r, g, b))
                }
                anim.start()
            }

            player.money.apply {
                set(status_bars.rubles, rubles, Currencies.RUBLES.id)
                set(status_bars.euros, euros, Currencies.EUROS.id)
                set(status_bars.bitcoins, bitcoins, Currencies.BITCOINS.id)
            }
        }


        override fun onConditionChanged(player: Player) {
            player.condition.apply {
                energy_bar.animatedProgress = energy
                fullness_bar.animatedProgress = fullness
                health_bar.animatedProgress = health
            }
        }

        override fun onMaxConditionChanged(player: Player) {
            player.maxCondition.apply {
                fullness_bar.max = fullness
                energy_bar.max = energy
                health_bar.max = health
            }
        }

        override fun onDead(player: Player, hunger: Boolean) {
            super.onDead(player, hunger)
            val dialog = AlertDialog.Builder(this@ContentActivity)
                    .setTitle("Бомж умер от " + if (hunger) "голода" else "болезни")
                    .setMessage(
                            (if (hunger) "Уровень сытости опустился ниже нуля!" else "Уровень здоровья опустился ниже нуля!") +
                                    " Вы можете воскресить его, посмотрев рекламу, или начать заново, потеряв прогресс"
                    )
                    .setIcon(R.drawable.dead)
                    .setCancelable(false)
                    .setNegativeButton("Начать заново") { _, _ ->
                        startActivity<MenuActivity>()
                    }
                    .setPositiveButton("Воскресить!", null)
                    .show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val res = ad.show {
                    longToast("Мы вас с того света достали! Пожалуйста, не забывайте о своем здоровье")
                    player.deadByZeroHealth = false
                    player.deadByHungry = false
                    player.condition.fullness = player.maxCondition.fullness
                    player.condition.health = player.maxCondition.health
                    onConditionChanged(player)
                    dialog.dismiss()
                }
                if (!res) {
                    toast(R.string.ad_loading)
                }
            }

        }

        override fun onCaughtByPolice(player: Player) {
            super.onCaughtByPolice(player)
            val dialog = AlertDialog.Builder(this@ContentActivity)
                    .setTitle("Вас поймали менты!")
                    .setMessage(
                            "Вы можете посмотреть рекламу, чтобы вас отпустили" +
                                    " или заплатить штраф в размере ${CurrentData.actionsBase.getPenalty(player)} рублей"
                    )
                    .setCancelable(false)
                    .setIcon(R.drawable.prison)
                    .setNegativeButton("Заплатить штраф", null)
                    .setPositiveButton("Не платить штраф", null)
                    .show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val res = ad.show {
                    longToast("Так уж и быть мы вас отпустим. Впредь будьте аккуратнее")
                    player.caughtByPolice = false
                    player.condition.fullness = player.maxCondition.fullness
                    player.condition.health = player.maxCondition.health
                    onConditionChanged(player)
                    dialog.dismiss()
                }
                if (!res) {
                    toast(R.string.ad_loading)
                }
            }

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener penalty@{
                if (player.addMoney((-CurrentData.actionsBase.getPenalty(player)).rub)) {
                    dialog.dismiss()
                    return@penalty
                }
                player.addMoney(CurrentData.exchange.convertWithCommission(player.money.euros, Currencies.EUROS, Currencies.RUBLES) of Currencies.RUBLES)
                player.money.euros = 0
                if (player.addMoney((-CurrentData.actionsBase.getPenalty(player)).rub)) {
                    longToast("Для выплаты штрафа все ваши евро были переведены в рубли")
                    dialog.dismiss()
                    return@penalty
                }

                player.addMoney(CurrentData.exchange.convertWithCommission(player.money.euros, Currencies.BITCOINS, Currencies.RUBLES) of Currencies.RUBLES)
                player.money.bitcoins = 0
                if (player.addMoney((-CurrentData.actionsBase.getPenalty(player)).rub)) {
                    longToast("Для выплаты штрафа все ваши евро и биткоины были переведены в рубли")
                    dialog.dismiss()
                    return@penalty
                }
                toast(getString(R.string.money_needed))
            }
        }


        override fun showRateDialog() {
            val contentIntent = PendingIntent.getActivity(
                    this@ContentActivity,
                    0,
                    Intent(this@ContentActivity, MenuActivity::class.java).apply { putExtra(RateDialog.RATE_DIALOG, true) },
                    PendingIntent.FLAG_CANCEL_CURRENT
            )

            val builder = NotificationCompat.Builder(this@ContentActivity)

            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Симулятор бомжа 2.0")
                    .setContentText("Нравится ли вам игра?")
                    .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(this@ContentActivity)
            notificationManager.notify(-8819293, builder.build())
        }

        override fun message(msg: String) = toast(msg)
    }
}
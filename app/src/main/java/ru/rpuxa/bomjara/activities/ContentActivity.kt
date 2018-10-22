package ru.rpuxa.bomjara.activities

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.status_bars.*
import kotlinx.android.synthetic.main.status_bars.view.*
import ru.rpuxa.bomjara.*
import ru.rpuxa.bomjara.R.drawable
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.game.CurrencyExchange
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.game.player.Money

class ContentActivity : AppCompatActivity() {

    val ad get() = (application as App).videoAd

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


        if (Player.CURRENT.old) {
            Player.CURRENT.old = false
            val gift = Actions.penalty
            AlertDialog.Builder(this)
                    .setTitle("Спасибо за установку обновления")
                    .setCancelable(false)
                    .setMessage("Мы полностью переработали дизайн и механику игры. С нововедениями можете ознакомиться " +
                            "при помощи подсказок. Так же держите от нас подарок - $gift рублей")
                    .setPositiveButton("Спасибо", null)
                    .show()
            Player.CURRENT.add(Money(rubles = gift.toLong()))
        }

        save()
    }

    override fun onPause() {
        save()
        super.onPause()
    }

    override fun onDestroy() {
        save()
        Player.CURRENT.listener = null
        super.onDestroy()
    }

    fun gotoMainMenu() {
        startActivity<MenuActivity>()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.exit_to_main_menu))
                .setPositiveButton(getString(R.string.exit)) { _, _ ->
                    gotoMainMenu()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
    }

    inner class PlayerListener : Player.Listener {

        override val activity get() = this@ContentActivity

        var anim = ValueAnimator.ofFloat()!!

        override fun onMoneyChanged(positive: Boolean, currency: Int, addCount: Long) {
            fun set(textView: TextView, count: Long, currency0: Int) {
                textView.text = count.divider()
                if (currency != currency0 || addCount == 0L)
                    return
                val startColor = if (positive) Color.GREEN else Color.RED
                val toColor = Color.WHITE
                anim.cancel()
                val anim = ValueAnimator.ofFloat(1f, 0f)
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
            Player.CURRENT.money.apply {
                set(status_bars.rubles, rubles, RUB)
                set(status_bars.euros, euros, EURO)
                set(status_bars.bitcoins, bitcoins, BITCOIN)
            }
        }


        override fun onConditionChanged() {
            Player.CURRENT.condition.apply {
                energy_bar.animatedProgress = energy
                fullness_bar.animatedProgress = fullness
                health_bar.animatedProgress = health
            }
        }

        override fun onMaxConditionChanged() {
            Player.CURRENT.maxCondition.apply {
                fullness_bar.max = fullness
                energy_bar.max = energy
                health_bar.max = health
            }
        }

        override fun onDead(hunger: Boolean) {
            super.onDead(hunger)
            val player = Player.CURRENT
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
                val res = ad.show {
                    toast("Мы вас с того света достали! Пожалуйста, не забывайте о своем здоровье", false)
                    player.deadByZeroHealth = false
                    player.deadByHungry = false
                    player.condition.fullness = player.maxCondition.fullness
                    player.condition.health = player.maxCondition.health
                    onConditionChanged()
                    dialog.dismiss()
                }
                if (!res) {
                    toast("Загрузка... Пожалуйста подождите")
                }
            }

        }

        override fun onCaughtByPolice() {
            super.onCaughtByPolice()
            val player = Player.CURRENT
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
                val res = ad.show {
                    toast("Так уж и быть мы вас отпустим. Впредь будьте аккуратнее", false)
                    player.caughtByPolice = false
                    player.condition.fullness = player.maxCondition.fullness
                    player.condition.health = player.maxCondition.health
                    onConditionChanged()
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
                Player.CURRENT.add(Money(rubles = CurrencyExchange.convert(Player.CURRENT.money.euros, EURO, RUB)))
                Player.CURRENT.money.euros = 0
                if (Player.CURRENT.add((-Actions.penalty).rub)) {
                    toast("Для выплаты штрафа все ваши евро были переведены в рубли", false)
                    dialog.dismiss()
                    return@penalty
                }

                Player.CURRENT.add(Money(rubles = CurrencyExchange.convert(Player.CURRENT.money.bitcoins, BITCOIN, RUB)))
                Player.CURRENT.money.bitcoins = 0
                if (Player.CURRENT.add((-Actions.penalty).rub)) {
                    toast("Для выплаты штрафа все ваши евро и биткоины были переведены в рубли", false)
                    dialog.dismiss()
                    return@penalty
                }
                toast(getString(R.string.money_needed))
            }
        }

        override fun showRateDialog() {
            //TODO
        }
    }

}
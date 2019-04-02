package ru.rpuxa.bomjara.refactor.v.activities

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.status_bars.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import ru.rpuxa.bomjara.BuildConfig
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.R.drawable.*
import ru.rpuxa.bomjara.refactor.m.player.bottle
import ru.rpuxa.bomjara.refactor.m.player.rub
import ru.rpuxa.bomjara.refactor.v.Bomjara
import ru.rpuxa.bomjara.refactor.v.ContentAdapter
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel
import ru.rpuxa.bomjara.utils.divider
import ru.rpuxa.bomjara.utils.getViewModel
import ru.rpuxa.bomjara.utils.observe
import ru.rpuxa.bomjara.utils.v


class ContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content)

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

        val playerViewModel = getViewModel<PlayerViewModel>()


        //DEBUG
        if (BuildConfig.DEBUG) {
            playerViewModel.addMoney(999999999.rub)
            playerViewModel.addMoney(999999999.bottle)
        }


        var lastRubles = -1L
        var lastBottles = -1L
        playerViewModel.money.observe(this) { money ->
            fun set(textView: TextView, count: Long, green: Boolean) {
                textView.text = count.divider()
                val startColor = if (green) Color.GREEN else Color.RED
                val toColor = Color.WHITE
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

            if (lastRubles == -1L) {
                rubles_bar.text = money.rubles.divider()
            } else {
                val deltaRubles = money.rubles - lastRubles
                if (deltaRubles != 0L) {
                    set(rubles_bar, money.rubles, deltaRubles > 0)
                }
            }
            if (lastBottles == -1L) {
                bottles_bar.text = money.bottles.divider()
            } else {
                val deltaBottles = money.bottles - lastBottles
                if (deltaBottles != 0L) {
                    set(bottles_bar, money.bottles, deltaBottles > 0)
                }
            }

            lastRubles = money.rubles
            lastBottles = money.bottles
        }


        playerViewModel.condition.observe(this) {
            it.apply {
                energy_bar.animatedProgress = energy
                fullness_bar.animatedProgress = fullness
                health_bar.animatedProgress = health
            }
        }

        playerViewModel.maxCondition.observe(this) {
            it.apply {
                fullness_bar.max = fullness
                energy_bar.max = energy
                health_bar.max = health
            }
        }

        playerViewModel.endGame.observe(this) {

            fun alivePlayer() {
                playerViewModel.addCondition(playerViewModel.maxCondition.v)
                playerViewModel.endGame.value = PlayerViewModel.ALIVE
            }

           fun dead(hunger: Boolean) {
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
                    val res = Bomjara.videoAd.show {
                        longToast("Мы вас с того света достали! Пожалуйста, не забывайте о своем здоровье")
                        alivePlayer()
                        dialog.dismiss()
                    }
                    if (!res) {
                        toast(R.string.ad_loading)
                    }
                }

            }

            when (it) {
                PlayerViewModel.CAUGHT_BY_POLICE -> {
                    val dialog = AlertDialog.Builder(this@ContentActivity)
                            .setTitle("Вас поймали менты!")
                            .setMessage(
                                    "Вы можете посмотреть рекламу, чтобы вас отпустили" +
                                            " или заплатить штраф в размере ${playerViewModel.penalty} рублей"
                            )
                            .setCancelable(false)
                            .setIcon(R.drawable.prison)
                            .setNegativeButton("Заплатить штраф", null)
                            .setPositiveButton("Не платить штраф", null)
                            .show()

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val res = Bomjara.videoAd.show {
                            longToast("Так уж и быть, мы вас отпустим. Впредь будьте аккуратнее")
                            alivePlayer()
                            dialog.dismiss()
                        }
                        if (!res) {
                            toast(R.string.ad_loading)
                        }
                    }

                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener penalty@{
                        if (playerViewModel.addMoney((-playerViewModel.penalty).rub)) {
                            dialog.dismiss()
                            return@penalty
                        }
                        toast(getString(R.string.money_needed))
                    }
                }

                PlayerViewModel.DEAD_BY_HEALTH -> dead(false)
                PlayerViewModel.DEAD_BY_HUNGRY -> dead(true)
            }
        }
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
}
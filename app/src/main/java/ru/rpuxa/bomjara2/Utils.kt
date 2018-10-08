package ru.rpuxa.bomjara2

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.Fragment
import android.widget.Toast
import ru.rpuxa.bomjara2.actions.Actions.ENERGY
import ru.rpuxa.bomjara2.actions.Actions.FOOD
import ru.rpuxa.bomjara2.actions.Actions.HEALTH
import ru.rpuxa.bomjara2.game.player.Money
import java.util.*
import kotlin.math.abs

val Int.rub get() = Money(rubles = toLong())
val Int.euro get() = Money(euros = toLong())
val Int.bitcoin get() = Money(bitcoins = toLong())
val Int.bottle get() = Money(bottles = toLong())

val FREE = Money()


const val RUB = 0
const val EURO = 1
const val BITCOIN = 2
const val BOTTLES = 3
const val DIAMONDS = 4

fun Context.getCurrencyIcon(money: Money) =
        BitmapFactory.decodeResource(
                resources,
                when (money.currency) {
                    RUB -> R.drawable.ruble
                    EURO -> R.drawable.euro
                    BITCOIN -> R.drawable.bitcoin
                    else -> R.drawable.bottle
                }
        )!!

fun Context.getMenuIcon(menu: Int) =
        BitmapFactory.decodeResource(
                resources,
                when (menu) {
                    ENERGY -> R.drawable.colored_energy
                    FOOD -> R.drawable.colored_food
                    HEALTH -> R.drawable.colored_health
                    else -> R.drawable.colored_job
                }
        )!!

fun Context.toast(msg: String, isShort: Boolean = true) =
        Toast.makeText(this, msg, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()

fun Fragment.toast(msg: String, isShort: Boolean = true) =
        context.toast(msg, isShort)


fun Long.divider(): String {
    var s = toString()
    var newString = ""
    while (s.length > 3) {
        newString = " " + s.substring(s.length - 3) + newString
        s = s.substring(0, s.length - 3)
    }

    return s + newString
}


val random = Random()

val gauss: Double
    get() {
        var gauss = 4.0
        while (abs(gauss) > 3.0)
            gauss = random.nextGaussian()
        gauss /= 6.0
        return gauss + 1.0
    }

fun Long.roundTimes(i: Double) = Math.ceil(this * i).toLong()
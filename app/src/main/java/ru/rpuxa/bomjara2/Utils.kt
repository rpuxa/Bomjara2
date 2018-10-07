package ru.rpuxa.bomjara2

import android.content.Context
import android.graphics.BitmapFactory
import ru.rpuxa.bomjara2.actions.Actions.ENERGY
import ru.rpuxa.bomjara2.actions.Actions.FOOD
import ru.rpuxa.bomjara2.actions.Actions.HEALTH
import ru.rpuxa.bomjara2.game.player.Money

val Int.rub get() = Money(rubles = toLong())
val Int.euro get() = Money(euros = toLong())
val Int.bitcoin get() = Money(bitcoins = toLong())
val Int.bottle get() = Money(bottles = toLong())

val FREE = Money()


const val RUB = 0
const val EURO = 1
const val BITCOIN = 2
const val BOTTLES = 3

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
                    ENERGY -> R.drawable.energy
                    FOOD -> R.drawable.food
                    HEALTH -> R.drawable.health
                    else -> R.drawable.job
                }
        )!!
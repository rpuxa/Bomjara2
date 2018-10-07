package ru.rpuxa.bomjara2.game.player

import ru.rpuxa.bomjara2.*

class Money(var rubles: Long = 0, var euros: Long = 0, var bitcoins: Long = 0, var bottles: Long = 0) {

    fun canAdd(money: Money) = when {
        rubles + money.rubles < 0 -> false
        euros + money.euros < 0 -> false
        bitcoins + money.bitcoins < 0 -> false
        bottles + money.bottles < 0 -> false
        else -> true
    }

    fun add(money: Money): Boolean {

        rubles += money.rubles
        euros += money.euros
        bitcoins += money.bitcoins
        bottles += money.bottles
        return true
    }

    val positive: Boolean
        get() = !(rubles < 0 || euros < 0 || bitcoins < 0 || bottles < 0)

    fun remove(money: Money) =
            add(-money)

    operator fun timesAssign(i: Double) {
        rubles = rubles.roundTimes(i)
        euros = euros.roundTimes(i)
        bitcoins = bitcoins.roundTimes(i)
        bottles = bottles.roundTimes(i)
    }

    operator fun times(i: Double) =
            Money(rubles, euros, bitcoins, bottles).apply {
                this *= i
            }

    operator fun unaryMinus() =
            Money(-rubles, -euros, -bitcoins, -bottles)

    override fun toString(): String {
        val c = when {
            euros != 0L -> euros
            bitcoins != 0L -> bitcoins
            bottles != 0L -> bottles
            else -> rubles
        }

        if (c == 0L)
            return "-0"

        return if (c > 0) "+$c" else "$c"
    }

    val currency
        get() = when {
            euros != 0L -> EURO
            bitcoins != 0L -> BITCOIN
            bottles != 0L -> BOTTLES
            else -> RUB
        }

}
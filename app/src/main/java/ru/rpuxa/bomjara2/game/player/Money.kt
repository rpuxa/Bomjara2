package ru.rpuxa.bomjara2.game.player

import ru.rpuxa.bomjara2.BITCOIN
import ru.rpuxa.bomjara2.BOTTLES
import ru.rpuxa.bomjara2.EURO
import ru.rpuxa.bomjara2.RUB

class Money(var rubles: Long = 0, var euros: Long = 0, var bitcoins: Long = 0, var bottles: Long = 0) {

    fun add(money: Money): Boolean {
        if (rubles + money.rubles < 0)
            return false
        if (euros + money.euros < 0)
            return false
        if (bitcoins + money.bitcoins < 0)
            return false
        if (bottles + money.bottles < 0)
            return false
        rubles += money.rubles
        euros += money.euros
        bitcoins += money.bitcoins
        bottles += money.bottles
        return true
    }

    fun remove(money: Money) =
            add(-money)


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
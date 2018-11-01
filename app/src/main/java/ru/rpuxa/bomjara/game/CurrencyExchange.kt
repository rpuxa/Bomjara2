package ru.rpuxa.bomjara.game

import ru.rpuxa.bomjara.BITCOIN
import ru.rpuxa.bomjara.EURO
import ru.rpuxa.bomjara.RUB
import ru.rpuxa.bomjara.game.player.Money
import java.math.BigInteger

object CurrencyExchange {
    private const val BOTTLES_RATE = 1.5

    private const val RUBLES_RATE = 1
    private const val EUROS_RATE = 70
    private const val BITCOINS_RATE = 2000

    fun handOverBottles(count: Long): Long {
        if (!Player.current.add(Money(bottles = -count)))
            throw IllegalStateException("Cant hand over more bottles than have")
        val rub = Math.ceil(BOTTLES_RATE * count).toLong()
        Player.current.add(Money(rubles = rub))
        return rub
    }


    fun convert(count: Long, from: Int, to: Int): Long {
        val big = BigInteger.valueOf(count) * BigInteger.valueOf(getRate(from).toLong()) / BigInteger.valueOf(getRate(to).toLong())
        return (big - big / BigInteger.valueOf(25L)).toLong()
    }


    private fun getRate(i: Int) = when (i) {
        RUB -> RUBLES_RATE
        EURO -> EUROS_RATE
        BITCOIN -> BITCOINS_RATE
        else -> throw UnsupportedOperationException("unknown currency")
    }
}
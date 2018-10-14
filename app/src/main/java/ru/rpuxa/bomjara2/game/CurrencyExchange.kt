package ru.rpuxa.bomjara2.game

import ru.rpuxa.bomjara2.BITCOIN
import ru.rpuxa.bomjara2.EURO
import ru.rpuxa.bomjara2.RUB
import ru.rpuxa.bomjara2.game.player.Money

object CurrencyExchange {
    private const val BOTTLES_RATE = 1.5

    private const val RUBLES_RATE = 1
    private const val EUROS_RATE = 70
    private const val BITCOINS_RATE = 2000

    fun handOverBottles(count: Long): Long {
        if (!Player.CURRENT.add(Money(bottles = -count)))
            throw IllegalStateException("Cant hand over more bottles than have")
        val rub = Math.ceil(BOTTLES_RATE * count).toLong()
        Player.CURRENT.add(Money(rubles = rub))
        return rub
    }


    fun convert(count: Long, from: Int, to: Int, reverse: Boolean) =
            ((if (reverse) 1.05 else .95) * count * getRate(from) / getRate(to)).toLong()


    private fun getRate(i: Int) = when (i) {
        RUB -> RUBLES_RATE
        EURO -> EUROS_RATE
        BITCOIN -> BITCOINS_RATE
        else -> throw UnsupportedOperationException("unknown currency")
    }
}
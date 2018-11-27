package ru.rpuxa.bomjara.impl.player

import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.api.player.CurrencyExchange
import java.math.BigDecimal

object CurrencyExchangeImpl : CurrencyExchange {
    override val commission = .06

    override fun convert(count: Long, from: Currencies, to: Currencies) =
            convertBigDecimal(count, from, to).toLong()

    override fun convertWithCommission(count: Long, from: Currencies, to: Currencies) =
            (convertBigDecimal(count, from, to) * BigDecimal(1 - commission)).toLong()

    private fun convertBigDecimal(count: Long, from: Currencies, to: Currencies) =
            BigDecimal(count) * BigDecimal(from.cost) / BigDecimal(to.cost)
}
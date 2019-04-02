package ru.rpuxa.bomjara.refactor.m.player

import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.api.player.Money

@Deprecated("")

open class MoneyImpl : Money {

    final override val currencies: LongArray

    constructor() {
        currencies = LongArray(Currencies.values().size)
    }

    constructor(currencies: LongArray) {
        this.currencies = currencies
    }

    override var rubles: Long
        get() = currencies[Currencies.RUBLES.id]
        set(value) {
            currencies[Currencies.RUBLES.id] = value
        }

    override var bottles: Long
        get() = currencies[Currencies.BOTTLES.id]
        set(value) {
            currencies[Currencies.BOTTLES.id] = value
        }

    override var diamonds: Long
        get() = currencies[Currencies.DIAMONDS.id]
        set(value) {
            currencies[Currencies.DIAMONDS.id] = value
        }


    override fun get(currencyId: Int) = currencies[currencyId]

    override fun canAdd(money: Money): Boolean {
        var i = 0
        return currencies.all { money.currencies[i++] + it >= 0 }
    }

    override fun addAssign(money: Money): Boolean {
        if (!canAdd(money))
            return false
        for (i in currencies.indices)
            currencies[i] += money.currencies[i]
        return true
    }

    override fun clone() = MoneyImpl(currencies.clone())
}
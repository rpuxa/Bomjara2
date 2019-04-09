package ru.rpuxa.bomjara.refactor.m.player.secure

import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.api.player.Money
import ru.rpuxa.bomjara.refactor.m.secure.SecureLong

open class SecureMoney(override val currencies: Array<SecureLong>) : Money {


    constructor() : this(Array(Currencies.values().size) { SecureLong(0) })


    override var rubles: Long
        get() = currencies[Currencies.RUBLES.id].value
        set(value) {
            currencies[Currencies.RUBLES.id].value = value
        }

    override var bottles: Long
        get() = currencies[Currencies.BOTTLES.id].value
        set(value) {
            currencies[Currencies.BOTTLES.id].value = value
        }

    override var diamonds: Long
        get() = currencies[Currencies.DIAMONDS.id].value
        set(value) {
            currencies[Currencies.DIAMONDS.id].value = value
        }


    override fun get(currencyId: Int) = currencies[currencyId].value

    override fun canAdd(money: Money): Boolean {
        var i = 0
        return currencies.all { money.currencies[i++].value + it.value >= 0 }
    }

    override fun addAssign(money: Money): Boolean {
        if (!canAdd(money))
            return false
        for (i in currencies.indices)
            currencies[i].value += money.currencies[i].value
        return true
    }

    override fun clone() = SecureMoney(Array(currencies.size) { SecureLong(currencies[it].value) })
}
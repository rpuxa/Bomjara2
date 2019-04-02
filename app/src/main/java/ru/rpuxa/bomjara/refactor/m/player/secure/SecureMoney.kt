package ru.rpuxa.bomjara.refactor.m.player.secure

import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.api.player.Money
import ru.rpuxa.bomjara.refactor.m.secure.SecureLong

open class SecureMoney(private val _currencies: Array<SecureLong>) : Money {

    override val currencies: LongArray
        get() = LongArray(_currencies.size) { _currencies[it].value }

    constructor() : this(Array(Currencies.values().size) { SecureLong(0) })


    override var rubles: Long
        get() = _currencies[Currencies.RUBLES.id].value
        set(value) {
            _currencies[Currencies.RUBLES.id].value = value
        }

    override var bottles: Long
        get() = _currencies[Currencies.BOTTLES.id].value
        set(value) {
            _currencies[Currencies.BOTTLES.id].value = value
        }

    override var diamonds: Long
        get() = _currencies[Currencies.DIAMONDS.id].value
        set(value) {
            _currencies[Currencies.DIAMONDS.id].value = value
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

    override fun clone() = SecureMoney(Array(_currencies.size) { SecureLong(_currencies[it].value) })
}
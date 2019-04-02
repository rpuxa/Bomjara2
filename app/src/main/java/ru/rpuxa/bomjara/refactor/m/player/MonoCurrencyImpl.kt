package ru.rpuxa.bomjara.refactor.m.player

import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.api.player.MonoCurrency
import ru.rpuxa.bomjara.refactor.m.player.secure.of
import java.lang.Math.ceil

@Deprecated("")

class MonoCurrencyImpl : MoneyImpl, MonoCurrency {

    override var count: Long
        set(value) {
            currencies[currency.id] = value
        }
        get() = currencies[currency.id]

    override var currency: Currencies

    constructor(count: Long, currency: Currencies) : super() {
        this.currency = currency
        this.count = count
    }

    constructor(currencies: LongArray, currency: Currencies) : super(currencies) {
        this.currency = currency
    }


    override fun multiplyAssign(x: Double) {
        count = ceil(count * x).toLong()
    }

    override fun invAssign() {
        count = -count
    }

    override fun toString(): String {
        val c = count
        if (c == 0L)
            return "-0"
        return if (c > 0) "+$c" else "$c"
    }

    override fun clone() = MonoCurrencyImpl(currencies.clone(), currency)

    companion object {
        @JvmField val ONE_DIAMOND: MonoCurrency = MonoCurrencyImpl(1, Currencies.DIAMONDS)
        @JvmField val THREE_DIAMONDS: MonoCurrency = MonoCurrencyImpl(3, Currencies.DIAMONDS)
    }
}

//infix fun Int.of(currency: Currencies) = MonoCurrencyImpl(toLong(), currency)
//
//infix fun Long.of(currency: Currencies) = MonoCurrencyImpl(this, currency)
//
inline val Int.rub get() = this of Currencies.RUBLES
inline val Int.bottle get() = this of Currencies.BOTTLES
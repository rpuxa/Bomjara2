package ru.rpuxa.bomjara.impl.player

import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.api.player.MonoCurrency
import java.lang.Math.ceil

class DefaultMonoCurrency : DefaultMoney, MonoCurrency {

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

    override fun clone() = DefaultMonoCurrency(currencies.clone(), currency)
}

infix fun Int.of(currencyId: Int) = this of Currencies.getById(currencyId)

infix fun Int.of(currency: Currencies) = DefaultMonoCurrency(toLong(), currency)

infix fun Long.of(currency: Currencies) = DefaultMonoCurrency(this, currency)

inline val Int.rub get() = this of Currencies.RUBLES
inline val Int.euro get() = this of Currencies.EUROS
inline val Int.bitcoin get() = this of Currencies.BITCOINS
inline val Int.bottle get() = this of Currencies.BOTTLES
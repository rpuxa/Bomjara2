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

    override fun clone() = DefaultMonoCurrency(currencies, currency)
}

 infix fun Int.of(currency: Currencies) = DefaultMonoCurrency(toLong(), currency)
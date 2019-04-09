package ru.rpuxa.bomjara.refactor.m.player.secure

import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.api.player.MonoCurrency


class SecureMonoCurrency(count: Long, override var currency: Currencies) : SecureMoney(), MonoCurrency {

    override var count: Long
        set(value) {
            currencies[currency.id].value = value
        }
        get() = currencies[currency.id].value

    init {
        this.count = count
    }

    override fun multiplyAssign(x: Double) {
        count = Math.ceil(count * x).toLong()
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

    override fun clone() = SecureMonoCurrency(count, currency)

    companion object {
        @JvmField
        val ONE_DIAMOND: MonoCurrency = SecureMonoCurrency(1, Currencies.DIAMONDS)
        @JvmField
        val THREE_DIAMONDS: MonoCurrency = SecureMonoCurrency(3, Currencies.DIAMONDS)
    }
}

infix fun Int.of(currency: Currencies) = SecureMonoCurrency(toLong(), currency)

infix fun Long.of(currency: Currencies) = SecureMonoCurrency(this, currency)
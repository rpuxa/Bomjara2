package ru.rpuxa.bomjara2.game.player

class Money(var rubles: Long = 0, var euros: Long = 0, var bitcoins: Long = 0) {

    fun add(money: Money): Boolean {
        if (rubles + money.rubles < 0)
            return false
        if (euros + money.euros < 0)
            return false
        if (bitcoins + money.bitcoins < 0)
            return false
        rubles += money.rubles
        euros += money.euros
        bitcoins += money.bitcoins
        return true
    }

    fun remove(money: Money) =
            add(-money)


    operator fun unaryMinus() =
            Money(-rubles, -euros, -bitcoins)
}
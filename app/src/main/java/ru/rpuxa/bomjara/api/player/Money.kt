package ru.rpuxa.bomjara.api.player

interface Money {

    var rubles: Long

    var euros: Long

    var bitcoins: Long

    var bottles: Long

    var diamonds: Long

    operator fun get(currency: Currencies) = get(currency.id)

    operator fun get(currencyId: Int)

    fun canAdd(money: Money): Boolean

    fun add(money: Money): Boolean

    fun multiply(x: Double): Money

    fun multiply(i: Int) = multiply(i.toDouble())

    fun inv(): Money

    override fun toString(): String
}
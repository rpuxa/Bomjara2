package ru.rpuxa.bomjara.api.player

interface Money {

    var rubles: Long

    var euros: Long

    var bitcoins: Long

    var bottles: Long

    var diamonds: Long

    val currencies: LongArray

    operator fun get(currency: Currencies) = get(currency.id)

    operator fun get(currencyId: Int): Long

    fun canAdd(money: Money): Boolean

    fun addAssign(money: Money): Boolean

    fun clone(): Money
}
package ru.rpuxa.bomjara.api.player

import ru.rpuxa.bomjara.refactor.m.secure.SecureLong

interface Money {

    var rubles: Long

    var bottles: Long

    var diamonds: Long

    val currencies: Array<SecureLong>

    operator fun get(currency: Currencies) = get(currency.id)

    operator fun get(currencyId: Int): Long

    fun canAdd(money: Money): Boolean

    fun addAssign(money: Money): Boolean

    fun clone(): Money
}
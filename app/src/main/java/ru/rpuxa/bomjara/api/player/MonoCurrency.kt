package ru.rpuxa.bomjara.api.player

interface MonoCurrency : Money {
    val count: Long

    val currency: Currencies

    val positive: Boolean get() = count > 0
}
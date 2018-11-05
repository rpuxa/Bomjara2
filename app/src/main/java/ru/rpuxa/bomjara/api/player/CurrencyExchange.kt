package ru.rpuxa.bomjara.api.player

interface CurrencyExchange {
    
    val commission: Double
    
    fun convert(count: Long, from: Currencies, to: Currencies): Long

    fun convertWithCommission(count: Long, from: Currencies, to: Currencies): Long
}
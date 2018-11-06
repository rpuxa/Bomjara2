package ru.rpuxa.bomjara.api.player

interface MonoCurrency : Money {
    val count: Long

    val currency: Currencies

    val positive: Boolean get() = count > 0

    fun multiplyAssign(x: Double)

    fun multiply(x: Double) = clone().apply { multiplyAssign(x) }

    fun multiply(i: Int) = multiply(i.toDouble())

    fun invAssign()

    fun inv(): MonoCurrency {
        val money = clone()
        money.invAssign()
        return money
    }

    override fun toString(): String

    override fun clone(): MonoCurrency
}
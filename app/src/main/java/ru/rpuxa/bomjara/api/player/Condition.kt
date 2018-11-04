package ru.rpuxa.bomjara.api.player

interface Condition {

    var energy: Int

    var fullness: Int

    var health: Int

    fun add(condition: Condition): Condition

    fun inv(): Condition

    fun truncate(maxCondition: Condition): Condition

    fun multiply(x: Double): Condition
}
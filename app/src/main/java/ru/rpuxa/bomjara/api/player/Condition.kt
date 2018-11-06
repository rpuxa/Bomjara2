package ru.rpuxa.bomjara.api.player

interface Condition {

    var energy: Int

    var fullness: Int

    var health: Int

    fun addAssign(condition: Condition)

    fun add(condition: Condition): Condition

    fun invAssign()

    fun inv(): Condition

    fun truncateAssign(maxCondition: Condition)

    fun truncate(maxCondition: Condition): Condition

    fun multiplyAssign(x: Double)

    fun multiply(x: Double): Condition

    fun clone(): Condition
}
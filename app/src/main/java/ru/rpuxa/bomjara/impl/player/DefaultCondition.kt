package ru.rpuxa.bomjara.impl.player

import ru.rpuxa.bomjara.api.player.Condition
import kotlin.math.min
import kotlin.math.round

class DefaultCondition(
        override var energy: Int = 0,
        override var fullness: Int = 0,
        override var health: Int = 0
) : Condition {

    override fun add(condition: Condition): Condition {
        val addedCondition = DefaultCondition(
                energy + condition.energy,
                fullness + condition.fullness,
                health + condition.health
        )

        if (addedCondition.energy < 0)
            addedCondition.energy = 0
        if (addedCondition.fullness < 0)
            addedCondition.fullness = 0
        if (addedCondition.health < 0)
            addedCondition.health = 0

        return addedCondition
    }

    override fun inv() = DefaultCondition(-energy, -fullness, -health)

    override fun truncate(maxCondition: Condition) =
            DefaultCondition(
                    min(maxCondition.energy, energy),
                    min(maxCondition.fullness, fullness),
                    min(maxCondition.health, health)
            )

    override fun multiply(x: Double) =
            DefaultCondition(
                    round(x * energy).toInt(),
                    round(x * fullness).toInt(),
                    round(x * health).toInt()
            )
}
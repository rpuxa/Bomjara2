package ru.rpuxa.bomjara.impl.player

import ru.rpuxa.bomjara.api.player.Condition
import kotlin.math.min
import kotlin.math.round

class ConditionImpl(
        override var energy: Int = 0,
        override var fullness: Int = 0,
        override var health: Int = 0
) : Condition {

    override fun addAssign(condition: Condition) {
        energy += condition.energy
        fullness += condition.fullness
        health += condition.health

        if (energy < 0)
            energy = 0
        if (fullness < 0)
            fullness = 0
        if (health < 0)
            health = 0
    }

    override fun add(condition: Condition) = clone().apply { addAssign(condition) }

    override fun invAssign() {
        energy = -energy
        fullness = -fullness
        health = -health
    }

    override fun inv() = ConditionImpl(-energy, -fullness, -health)

    override fun truncateAssign(maxCondition: Condition) {
        energy = min(maxCondition.energy, energy)
        fullness = min(maxCondition.fullness, fullness)
        health = min(maxCondition.health, health)
    }

    override fun truncate(maxCondition: Condition) = clone().apply { truncateAssign(maxCondition) }

    override fun multiplyAssign(x: Double) {
        energy = round(x * energy).toInt()
        fullness = round(x * fullness).toInt()
        health = round(x * health).toInt()
    }

    override fun multiply(x: Double) = clone().apply { multiplyAssign(x) }

    override fun clone() = ConditionImpl(energy, fullness, health)
}
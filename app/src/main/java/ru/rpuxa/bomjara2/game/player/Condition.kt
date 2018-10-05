package ru.rpuxa.bomjara2.game.player

class Condition(var energy: Int = 0,
                var fullness: Int = 0,
                var health: Int = 0
) {

    operator fun plusAssign(condition: Condition) {
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

    operator fun unaryMinus() =
            Condition(-energy, -fullness, -health)

    operator fun minusAssign(condition: Condition) {
        this += -condition
    }

    fun truncate(maxCondition: Condition) {
        if (energy > maxCondition.energy)
            energy = maxCondition.energy
        if (fullness > maxCondition.fullness)
            fullness = maxCondition.fullness
        if (health > maxCondition.health)
            health = maxCondition.health
    }
}
package ru.rpuxa.bomjara.refactor.m.player.secure

import ru.rpuxa.bomjara.api.player.Condition
import ru.rpuxa.bomjara.refactor.m.secure.SecureInt
import kotlin.math.min
import kotlin.math.round

class SecureCondition(energy: Int, fullness: Int, health: Int) : Condition {
    private val _energy = SecureInt(energy)
    private val _fullness = SecureInt(fullness)
    private val _health = SecureInt(health)

    override var energy: Int
        get() = _energy.value
        set(value) {
            _energy.value = value
        }
    override var fullness: Int
        get() = _fullness.value
        set(value) {
            _fullness.value = value
        }
    override var health: Int
        get() = _health.value
        set(value) {
            _health.value = value
        }

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

    override fun inv() = SecureCondition(-energy, -fullness, -health)

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

    override fun clone() = SecureCondition(energy, fullness, health)
}

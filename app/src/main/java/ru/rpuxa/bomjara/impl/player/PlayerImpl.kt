package ru.rpuxa.bomjara.impl.player

import ru.rpuxa.bomjara.api.player.*
import ru.rpuxa.bomjara.utils.gauss

open class PlayerImpl(
        final override val id: Long,
        final override var name: String,
        final override val condition: Condition,
        final override val maxCondition: Condition,
        final override val money: Money,
        final override val possessions: Possessions,
        final override val courses: CompletedCourses,
        final override var age: Int,
        final override var efficiency: Int,
        final override var caughtByPolice: Boolean,
        final override var deadByZeroHealth: Boolean,
        final override var deadByHungry: Boolean
) : Player {

    final override var daysWithoutCaught = 0
    final override var doingAction = false

    final override var listener: Player.Listener? = null
        set(value) {
            field = value
            if (value != null)
                update(value)
        }

    private fun update(listener: Player.Listener) {
        listener.onMoneyChanged(this, false, Currencies.NONE.id, 0L)
        listener.onConditionChanged(this)
        listener.onMaxConditionChanged(this)
        when {
            deadByZeroHealth -> listener.onDead(this, false)
            deadByHungry -> listener.onDead(this, true)
            caughtByPolice -> listener.onCaughtByPolice(this)
        }
    }

    override fun addCondition(add: Condition) {
        condition.addAssign(add.multiply(gauss))
        condition.truncateAssign(maxCondition)
        listener?.onConditionChanged(this)
        if (condition.health == 0)
            listener?.onDead(this, false)
        else if (condition.fullness == 0)
            listener?.onDead(this, true)
    }

    override fun addMoney(add: MonoCurrency): Boolean {
        if (!money.addAssign(add))
            return false
        listener?.onMoneyChanged(this, add.positive, add.currency.id, add.count)
        return true
    }

    override fun addSalary(money: MonoCurrency) {
        addMoney(money
                .multiply(gauss)
                .multiply(efficiency.toDouble() / 100)
        )
    }

    override fun addDiamond() {
        money.diamonds++
    }

    override fun dispatchListener() {
        listener = null
    }
}
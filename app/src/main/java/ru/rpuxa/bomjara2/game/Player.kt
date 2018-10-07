package ru.rpuxa.bomjara2.game

import ru.rpuxa.bomjara2.game.player.Condition
import ru.rpuxa.bomjara2.game.player.Money
import ru.rpuxa.bomjara2.game.player.Possessions
import ru.rpuxa.bomjara2.gauss

class Player {
    var listener: Player.Listener? = null
        set(value) {
            field = value
            if (value != null)
                update(value)
        }

    var condition = Condition(75, 75, 100)
    var maxCondition = Condition(100, 100, 100)
    var money = Money(rubles = 20000)
    var possessions = Possessions()

    private var caughtByPolice = false
    private var dead = false

    var doingAction = false


    operator fun plusAssign(condition: Condition) {
        this.condition += condition
        this.condition.truncate(maxCondition)
        listener?.onConditionChanged(this.condition, this, maxCondition)
    }

    operator fun minusAssign(condition: Condition) {
        this += -condition
    }

    fun add(money: Money): Boolean {
        if (!this.money.add(money)) {
            // listener?.onMoneyNeeded()
            return false
        }
        listener?.onMoneyChanged(this.money, this, money.positive)
        return true
    }

    fun salary(money: Money) {
        add(money * gauss)
    }

    private fun update(listener: Listener) {
        listener.onMoneyChanged(money, this, false)
        listener.onConditionChanged(condition, this, maxCondition)
        if (dead)
            listener.onDead()
        else if (caughtByPolice)
            listener.onCaughtByPolice()
    }

    interface Listener {

        //  fun onMoneyNeeded()

        fun onDead()

        fun onCaughtByPolice()

        fun onMoneyChanged(money: Money, player: Player, positive: Boolean)

        fun onConditionChanged(condition: Condition, player: Player, maxCondition: Condition)
    }

    companion object {
        var CURRENT = Player()
    }
}
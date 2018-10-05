package ru.rpuxa.bomjara2.game

import ru.rpuxa.bomjara2.game.player.Condition
import ru.rpuxa.bomjara2.game.player.Money

class Player {
    var listener: Player.Listener? = null
    var condition = Condition(75, 75, 100)
    var maxCondition = Condition(100, 100, 100)
    var money = Money(rubles = 200)

    operator fun plusAssign(condition: Condition) {
        this.condition += condition
        condition.truncate(maxCondition)
    }

    operator fun minusAssign(condition: Condition) {
        this += -condition
    }

    fun add(money: Money): Boolean {
        if (!this.money.add(money)) {
            listener?.onMoneyNeeded()
            return false
        }

        return true
    }

    fun remove(money: Money) =
            add(-money)


    interface Listener {

        fun onMoneyNeeded()

        fun caughtByPolice()
    }
}
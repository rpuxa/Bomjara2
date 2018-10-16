package ru.rpuxa.bomjara2.game

import android.support.annotation.CallSuper
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.game.player.Condition
import ru.rpuxa.bomjara2.game.player.Money
import ru.rpuxa.bomjara2.game.player.Possessions
import ru.rpuxa.bomjara2.gauss
import ru.rpuxa.bomjara2.getStringAge
import ru.rpuxa.bomjara2.save.Save

class Player(var id: Long, var name: String, val old: Boolean) {
    var listener: Player.Listener? = null
        set(value) {
            field = value
            if (value != null)
                update(value)
        }

    var condition = Condition(75, 75, 100)
    var maxCondition = Condition(100, 100, 100)
    var money = Money(rubles = 20000)
    var possessions = Possessions(location = 2)
    var courses = IntArray(Actions.COURSES.size)
    var age = 0
    var efficiency = 100

    var caughtByPolice = false
    var deadByZeroHealth = false
    var deadByHungry = false


    var doingAction = false
    operator fun plusAssign(add: Condition) {
        condition += add * gauss
        condition.truncate(maxCondition)
        listener?.onConditionChanged(condition, this, maxCondition)
        if (add.health == 0)
            listener?.onDead(this, false)
        else if (add.fullness == 0)
            listener?.onDead(this, true)
    }

    fun add(money: Money): Boolean {
        if (!this.money.add(money))
            return false
        listener?.onMoneyChanged(this.money, this, money.positive)
        return true
    }

    fun salary(money: Money) {
        add(money * (.5 + condition.energy.toDouble() / maxCondition.energy.toDouble()) * gauss * (efficiency.toDouble() / 100))
    }

    private fun update(listener: Listener) {
        listener.onMoneyChanged(money, this, false)
        listener.onConditionChanged(condition, this, maxCondition)
        when {
            deadByZeroHealth -> listener.onDead(this, false)
            deadByHungry -> listener.onDead(this, true)
            caughtByPolice -> listener.onCaughtByPolice(this)
        }

    }

    val stringAge get() = getStringAge(age)

    interface Listener {

        @CallSuper
        fun onDead(player: Player, hunger: Boolean) {
            if (hunger)
                player.deadByHungry = true
            else
                player.deadByZeroHealth = true
        }

        @CallSuper
        fun onCaughtByPolice(player: Player) {
            player.caughtByPolice = true
        }

        fun onMoneyChanged(money: Money, player: Player, positive: Boolean)

        fun onConditionChanged(condition: Condition, player: Player, maxCondition: Condition)
    }

    companion object {
        lateinit var CURRENT: Player


        fun fromSave(save: Save) = Player(save.id, save.name, save.old).apply {
            age = save.age
            money = Money(save.rubles, save.euros, save.bitcoins, save.bottles, save.diamonds)
            possessions = Possessions(save.transport, save.home, save.friend, save.location)
            efficiency = save.efficiency
            maxCondition = Condition(save.maxEnergy, save.maxFullness, save.maxHealth)
            condition = Condition(save.energy, save.fullness, save.health)
            courses = save.courses
            deadByHungry = save.deadByHungry
            deadByZeroHealth = save.deadByZeroHealth
            caughtByPolice = save.caughtByPolice
        }
    }


    fun toSave() =
            Save(
                    id,
                    old,
                    name,
                    age,
                    money.bottles,
                    money.rubles,
                    money.euros,
                    money.bitcoins,
                    money.diamonds,
                    possessions.location,
                    possessions.friend,
                    possessions.home,
                    possessions.transport,
                    efficiency,
                    maxCondition.energy,
                    maxCondition.fullness,
                    maxCondition.health,
                    condition.energy,
                    condition.fullness,
                    condition.health,
                    courses,
                    deadByHungry,
                    deadByZeroHealth,
                    caughtByPolice
            )


}
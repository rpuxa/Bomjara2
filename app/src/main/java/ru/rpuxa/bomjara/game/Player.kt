package ru.rpuxa.bomjara.game

import android.support.annotation.CallSuper
import android.support.v4.app.FragmentActivity
import ru.rpuxa.bomjara.NONE
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.game.player.Condition
import ru.rpuxa.bomjara.game.player.Money
import ru.rpuxa.bomjara.game.player.Possessions
import ru.rpuxa.bomjara.gauss
import ru.rpuxa.bomjara.getStringAge
import ru.rpuxa.bomjara.save.Save
import ru.rpuxa.bomjara.toast

class Player(var id: Long, var name: String, var old: Boolean) {
    var listener: Player.Listener? = null
        set(value) {
            field = value
            if (value != null)
                update(value)
        }

    var condition = Condition(75, 75, 100)
    var maxCondition = Condition(100, 100, 100)
    var money = Money(rubles = 50)
    var possessions = Possessions()
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
        listener?.onConditionChanged()
        if (condition.health == 0)
            listener?.onDead(false)
        else if (condition.fullness == 0)
            listener?.onDead(true)
    }

    fun add(money: Money): Boolean {
        if (!this.money.add(money))
            return false
        listener?.onMoneyChanged(money.positive, money.currency)
        return true
    }

    fun salary(money: Money) {
        add(money * (.5 + condition.energy.toDouble() / maxCondition.energy.toDouble()) * gauss * (efficiency.toDouble() / 100))
    }

    private fun update(listener: Listener) {
        listener.onMoneyChanged(false, NONE)
        listener.onConditionChanged()
        listener.onMaxConditionChanged()
        when {
            deadByZeroHealth -> listener.onDead(false)
            deadByHungry -> listener.onDead(true)
            caughtByPolice -> listener.onCaughtByPolice()
        }

    }

    val stringAge get() = getStringAge(age)

    interface Listener {

        @CallSuper
        fun onDead(hunger: Boolean) {
            val player = Player.CURRENT
            if (hunger)
                player.deadByHungry = true
            else
                player.deadByZeroHealth = true
        }

        @CallSuper
        fun onCaughtByPolice() {
            Player.CURRENT.caughtByPolice = true
        }

        fun onMoneyChanged(positive: Boolean, currency: Int)

        fun onMaxConditionChanged()

        fun onConditionChanged()

        val activity: FragmentActivity
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


    fun toSave() = Save(
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

    fun addDiamond() {
        money.diamonds++
        listener?.activity?.toast("Вы нашли алмаз!")
    }


}
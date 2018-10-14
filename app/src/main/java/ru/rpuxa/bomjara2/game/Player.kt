package ru.rpuxa.bomjara2.game

import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.game.player.Condition
import ru.rpuxa.bomjara2.game.player.Money
import ru.rpuxa.bomjara2.game.player.Possessions
import ru.rpuxa.bomjara2.gauss
import ru.rpuxa.bomjara2.save.Save

class Player(var name: String) {
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

    private var caughtByPolice = false

    private var dead = false
    var doingAction = false
    operator fun plusAssign(condition: Condition) {
        this.condition += condition * gauss
        this.condition.truncate(maxCondition)
        listener?.onConditionChanged(this.condition, this, maxCondition)
    }

    fun add(money: Money): Boolean {
        if (!this.money.add(money))
            return false
        listener?.onMoneyChanged(this.money, this, money.positive)
        return true
    }

    fun salary(money: Money) {
        add(money * gauss * (efficiency.toDouble() / 100))
    }

    private fun update(listener: Listener) {
        listener.onMoneyChanged(money, this, false)
        listener.onConditionChanged(condition, this, maxCondition)
        if (dead)
            listener.onDead(this)
        else if (caughtByPolice)
            listener.onCaughtByPolice(this)
    }

    val stringAge get() = "${25 + age / 365} лет ${age % 365} дней"

    interface Listener {

        fun onDead(player: Player) {
            player.dead = true
        }

        fun onCaughtByPolice(player: Player) {
            player.caughtByPolice = true
        }

        fun onMoneyChanged(money: Money, player: Player, positive: Boolean)

        fun onConditionChanged(condition: Condition, player: Player, maxCondition: Condition)
    }

    companion object {
        var CURRENT = Player("васёк")


        fun fromSave(save: Save) = Player(save.name).apply {
            age = save.age
            money = Money(save.rubles, save.euros, save.bitcoins, save.bottles, save.diamonds)
            possessions = Possessions(save.transport, save.home, save.friend, save.location)
            efficiency = save.efficiency
            maxCondition = Condition(save.maxEnergy, save.maxFullness, save.maxHealth)
            condition = Condition(save.energy, save.fullness, save.health)
            courses = save.courses
        }
    }


    fun toSave() =
            Save(
                    false,
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
                    courses
            )


}
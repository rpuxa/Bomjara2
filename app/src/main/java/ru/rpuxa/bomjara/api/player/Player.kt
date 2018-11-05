package ru.rpuxa.bomjara.api.player

import android.support.annotation.CallSuper

typealias CompletedCourses = IntArray

interface Player {
    val id: Long

    var name: String

    var old: Boolean

    var listener: Listener?

    val condition: Condition

    val maxCondition: Condition

    val money: Money

    val possessions: Possessions

    val courses: CompletedCourses

    var age: Int

    var efficiency: Int

    var daysWithoutCaught: Int

    var caughtByPolice: Boolean

    var deadByZeroHealth: Boolean

    var deadByHungry: Boolean

    var doingAction: Boolean


    fun addCondition(condition: Condition)

    fun addMoney(money: MonoCurrency): Boolean

    fun addSalary(money: MonoCurrency)

    fun addDiamond()

    fun dispatchListener()

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
            player.daysWithoutCaught = 0
            player.caughtByPolice = true
        }

        fun onMoneyChanged(player: Player, positive: Boolean, currency: Int, addCount: Long)

        fun onMaxConditionChanged(player: Player)

        fun onConditionChanged(player: Player)

        fun showRateDialog()
    }
}
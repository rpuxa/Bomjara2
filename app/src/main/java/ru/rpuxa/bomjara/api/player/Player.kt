package ru.rpuxa.bomjara.api.player

import android.support.v4.app.FragmentActivity

typealias CompletedCourses = IntArray

interface Player {
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


    interface Listener {
        fun onDead(hunger: Boolean)

        fun onCaughtByPolice()

        fun onMoneyChanged(positive: Boolean, currency: Int, addCount: Long)

        fun onMaxConditionChanged()

        fun onConditionChanged()

        fun showRateDialog()

        val activity: FragmentActivity
    }
}
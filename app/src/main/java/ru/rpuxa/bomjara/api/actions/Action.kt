package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.api.player.Condition
import ru.rpuxa.bomjara.api.player.Money
import ru.rpuxa.bomjara.api.player.Player

typealias ActionId = Int

interface Action {

    val id: ActionId

    val level: Int

    val menu: Int

    val name: String

    val addMoney: Money

    val addCondition: Condition

    val illegal: Boolean


    fun canPerform(player: Player): Int

    fun perform(player: Player): Boolean

    companion object {
        const val NOTHING_NEEDED = 0
        const val MONEY_NEEDED = 1
        const val ENERGY_NEEDED = 2
    }
}
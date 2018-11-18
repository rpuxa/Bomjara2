package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.api.actions.auxiliary.HasId
import ru.rpuxa.bomjara.api.actions.auxiliary.Nameable
import ru.rpuxa.bomjara.api.player.Condition
import ru.rpuxa.bomjara.api.player.MonoCurrency
import ru.rpuxa.bomjara.api.player.Player


/**
 * API for action. Using actions, player can replenish health, fullness, energy or money
 */
interface Action : HasId, Nameable {


    /**
     * From which friend (for job) or location (for other actions) available this action
     */
    val level: Int


    /**
     * In which menu you can find this action
     */
    val menu: Int


    /**
     * How much money you get from this action (or spent if count is negative)
     */
    val addMoney: MonoCurrency

    /**
     * How much energy, health and food you get from this action
     */
    val addCondition: Condition

    /**
     * Is action illegal
     */
    val illegal: Boolean


    /**
     * Check whether a player can perform this action.
     * @returns
     * [NOTHING_NEEDED] if he can
     * [MONEY_NEEDED] if player needed money
     * [ENERGY_NEEDED] if player needed energy
     */
    fun canPerform(player: Player): Int

    /**
     * Perform this action. Must invoke [canPerform] at the beginning
     * @returns true if the action was performed else false
     */
    fun perform(player: Player): Boolean

    companion object {
        const val NOTHING_NEEDED = 0
        const val MONEY_NEEDED = 1
        const val ENERGY_NEEDED = 2
    }
}
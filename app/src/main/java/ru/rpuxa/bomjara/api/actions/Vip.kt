package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.api.actions.auxiliary.CanBuy
import ru.rpuxa.bomjara.api.actions.auxiliary.HasId
import ru.rpuxa.bomjara.api.actions.auxiliary.Nameable
import ru.rpuxa.bomjara.api.player.MonoCurrency
import ru.rpuxa.bomjara.api.player.Player

/**
 * API for vip goods. Vip goods you can buy only for diamonds
 */
interface Vip : HasId, Nameable, CanBuy {

    /**
     * Callback. Invokes when player bought this vip
     */
    val onBuy: (Player) -> Unit

    /**
     * Try to buy this vip
     * @returns true if purchase was successful, else false
     */
    fun buy(player: Player): Boolean

    override val cost: MonoCurrency
}
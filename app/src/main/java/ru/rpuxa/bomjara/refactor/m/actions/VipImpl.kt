package ru.rpuxa.bomjara.refactor.m.actions

import ru.rpuxa.bomjara.api.actions.Player
import ru.rpuxa.bomjara.api.actions.Vip
import ru.rpuxa.bomjara.api.player.MonoCurrency

class VipImpl(
        override val id: Int,
        override val name: String,
        override val cost: MonoCurrency,
        override val onBuy: (Player) -> Unit
) : Vip {

    override fun buy(player: Player): Boolean {
        if (!player.addMoney(cost.inv()))
            return false
        onBuy(player)
        return true
    }
}
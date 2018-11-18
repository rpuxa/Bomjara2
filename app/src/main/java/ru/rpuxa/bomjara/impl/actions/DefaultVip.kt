package ru.rpuxa.bomjara.impl.actions

import ru.rpuxa.bomjara.api.actions.Vip
import ru.rpuxa.bomjara.api.player.MonoCurrency
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.impl.Data.statistic

class DefaultVip(
        override val id: Int,
        override val name: String,
        override val cost: MonoCurrency,
        override val onBuy: (Player) -> Unit
) : Vip {



    override fun buy(player: Player): Boolean {
        if (!player.addMoney(cost.inv()))
            return false
        onBuy(player)
        statistic.countVip(id)
        return true
    }
}
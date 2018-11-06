package ru.rpuxa.bomjara.impl.actions

import ru.rpuxa.bomjara.api.actions.Vip
import ru.rpuxa.bomjara.api.actions.VipId
import ru.rpuxa.bomjara.api.player.Currencies.DIAMONDS
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.impl.Data.statistic
import ru.rpuxa.bomjara.impl.player.of

class DefaultVip(
        override val id: VipId,
        override val name: String,
        override val cost: Int,
        override val onBuy: (Player) -> Unit
) : Vip {



    override fun buy(player: Player): Boolean {
        if (!player.addMoney(cost of DIAMONDS))
            return false
        onBuy(player)
        statistic.countVip(id)
        return true
    }
}
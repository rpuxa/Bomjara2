package ru.rpuxa.bomjara.actions

import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.game.player.Money
import ru.rpuxa.bomjara.statistic.Statistic

class Vip(val id: Int, val name: String, val cost: Int, val onBuy: (Player) -> Unit) {

    fun buy(): Boolean {
        if (!Player.current.add(-Money(diamonds = cost.toLong())))
            return false
        onBuy(Player.current)
        Statistic.countVip(id)
        return true
    }
}
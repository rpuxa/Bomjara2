package ru.rpuxa.bomjara.actions

import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.game.player.Money

class Vip(val id: Int, val name: String, val cost: Int, val onBuy: (Player) -> Unit) {

    fun buy(): Boolean {
        if (!Player.CURRENT.add(-Money(diamonds = cost.toLong())))
            return false
        onBuy(Player.CURRENT)
        return true
    }
}
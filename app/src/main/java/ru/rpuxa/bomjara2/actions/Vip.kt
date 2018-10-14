package ru.rpuxa.bomjara2.actions

import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.game.player.Money

class Vip(val name: String, val cost: Int, val onBuy: (Player) -> Unit) {

    fun buy(): Boolean {
        if (!Player.CURRENT.add(-Money(diamonds = cost.toLong())))
            return false
        onBuy(Player.CURRENT)
        return true
    }
}
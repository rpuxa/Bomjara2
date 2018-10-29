package ru.rpuxa.bomjara.actions

import ru.rpuxa.bomjara.game.player.Money

class Course(
        val id: Int,
        val name: String,
        val money: Money,
        val length: Int
) {
    val skipCost = money * 4.0
}

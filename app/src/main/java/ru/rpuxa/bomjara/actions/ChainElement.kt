package ru.rpuxa.bomjara.actions

import ru.rpuxa.bomjara.game.player.Money
import ru.rpuxa.bomjara.game.player.Possessions

class ChainElement(val name: String, transport: Int, home: Int, friend: Int, location: Int, val course: Int, val moneyRemove: Money) {
    val possessions = Possessions(transport, home, friend, location)
}


package ru.rpuxa.bomjara2.actions

import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.game.player.Condition
import ru.rpuxa.bomjara2.game.player.Money
import java.util.*

class Action(val name: String, val addMoney: Money, val addCondition: Condition, val illegal: Boolean = false) {

    fun perform(player: Player) {
        if (!player.add(addMoney))
            return
        player += addCondition
        if (illegal && random.nextInt(10) == 5) {
            player.listener?.caughtByPolice()
        }
    }

    companion object {
        private val random = Random()
    }
}
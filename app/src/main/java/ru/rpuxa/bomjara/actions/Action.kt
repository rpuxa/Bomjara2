package ru.rpuxa.bomjara.actions

import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.game.player.Condition
import ru.rpuxa.bomjara.game.player.Money
import ru.rpuxa.bomjara.random

class Action(val name: String, val addMoney: Money, val addCondition: Condition, val illegal: Boolean = false) {


    fun canPerform(player: Player) =
            when {
                !player.money.canAdd(addMoney) -> MONEY_NEEDED
                addMoney.positive && player.condition.energy == 0 -> ENERGY_NEEDED
                else -> YES
            }



    fun perform(player: Player) {
        if (canPerform(player) != YES)
            return
        player.age++
        if (addMoney.positive)
            player.salary(addMoney)
        else
            player.add(addMoney)
        player += addCondition
        if (illegal && random.nextInt(10) == 5) {
            player.listener?.onCaughtByPolice(player)
        }
    }

    companion object {
        const val YES = 0
        const val MONEY_NEEDED = 1
        const val ENERGY_NEEDED = 2
    }
}
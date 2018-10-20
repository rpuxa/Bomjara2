package ru.rpuxa.bomjara.actions

import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.game.player.Condition
import ru.rpuxa.bomjara.game.player.Money
import ru.rpuxa.bomjara.random

class Action(
        val name: String,
        val addMoney: Money,
        val addCondition: Condition,
        val illegal: Boolean = false
) {


    fun canPerform(player: Player) =
            when {
                !player.money.canAdd(addMoney) -> MONEY_NEEDED
                addMoney.positive && player.condition.energy == 0 -> ENERGY_NEEDED
                else -> NOTHING_NEEDED
            }


    fun perform(player: Player) {
        if (canPerform(player) != NOTHING_NEEDED)
            return
        player.age++
        if (addMoney.positive)
            player.salary(addMoney)
        else
            player.add(addMoney)
        player += addCondition
        if (random.nextInt(40) == 10) {
            player.addDiamond()
        }
        if (illegal && random.nextInt(10) == 5) {
            player.listener?.onCaughtByPolice()
        }
    }

    companion object {
        const val NOTHING_NEEDED = 0
        const val MONEY_NEEDED = 1
        const val ENERGY_NEEDED = 2
    }
}
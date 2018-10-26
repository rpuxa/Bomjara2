package ru.rpuxa.bomjara.actions

import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.game.player.Condition
import ru.rpuxa.bomjara.game.player.Money
import ru.rpuxa.bomjara.random
import ru.rpuxa.bomjara.settings.settings
import ru.rpuxa.bomjara.statistic.Statistic
import ru.rpuxa.bomjara.views.RateDialog

class Action(
        val id: Int,
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
        if (illegal && player.daysWithoutCaught >= 2 && random.nextInt(10) == 5) {
            player.listener?.onCaughtByPolice()
        } else {
            player.daysWithoutCaught++
            if (!settings.wasRated && !RateDialog.shown && player.age > 100) {
                player.listener?.showRateDialog()
            }
        }
        Statistic.countAction(id)
    }

    companion object {
        const val NOTHING_NEEDED = 0
        const val MONEY_NEEDED = 1
        const val ENERGY_NEEDED = 2
    }
}
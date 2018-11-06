package ru.rpuxa.bomjara.impl.actions

import ru.rpuxa.bomjara.api.actions.Action
import ru.rpuxa.bomjara.api.actions.Action.Companion.ENERGY_NEEDED
import ru.rpuxa.bomjara.api.actions.Action.Companion.MONEY_NEEDED
import ru.rpuxa.bomjara.api.actions.Action.Companion.NOTHING_NEEDED
import ru.rpuxa.bomjara.api.actions.ActionId
import ru.rpuxa.bomjara.api.player.Condition
import ru.rpuxa.bomjara.api.player.MonoCurrency
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.impl.random
import ru.rpuxa.bomjara.impl.Data.settings
import ru.rpuxa.bomjara.impl.Data.statistic
import ru.rpuxa.bomjara.impl.statistic.DefaultStatistic
import ru.rpuxa.bomjara.impl.views.RateDialog

class DefaultAction(
        override val id: ActionId,
        override val level: Int,
        override val menu: Int,
        override val name: String,
        override val addMoney: MonoCurrency,
        override val addCondition: Condition,
        override val illegal: Boolean
) : Action {


    override fun canPerform(player: Player) = when {
        !player.money.canAdd(addMoney) -> MONEY_NEEDED
        addMoney.positive && player.condition.energy == 0 -> ENERGY_NEEDED
        else -> NOTHING_NEEDED
    }


    override fun perform(player: Player): Boolean {
        if (canPerform(player) != NOTHING_NEEDED)
            return false
        player.age++
        if (addMoney.positive)
            player.addSalary(addMoney)
        else
            player.addMoney(addMoney)
        player.addCondition(addCondition)
        if (random.nextInt(40) == 10)
            player.addDiamond()
        if (illegal && player.daysWithoutCaught >= 2 && random.nextInt(10) == 5) {
            player.listener?.onCaughtByPolice(player)
        } else {
            player.daysWithoutCaught++
            if (!settings.wasRated && !RateDialog.shown && player.age > 100) {
                player.listener?.showRateDialog()
            }
        }
        statistic.countAction(id)
        return true
    }
}
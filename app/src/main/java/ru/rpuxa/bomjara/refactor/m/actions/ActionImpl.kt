package ru.rpuxa.bomjara.refactor.m.actions

import ru.rpuxa.bomjara.api.actions.Action
import ru.rpuxa.bomjara.api.actions.Action.Companion.ENERGY_NEEDED
import ru.rpuxa.bomjara.api.actions.Action.Companion.MONEY_NEEDED
import ru.rpuxa.bomjara.api.actions.Action.Companion.NOTHING_NEEDED
import ru.rpuxa.bomjara.api.actions.Player
import ru.rpuxa.bomjara.api.player.Condition
import ru.rpuxa.bomjara.api.player.MonoCurrency
import ru.rpuxa.bomjara.refactor.m.player.MonoCurrencyImpl
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel
import ru.rpuxa.bomjara.utils.nnValue
import ru.rpuxa.bomjara.utils.random
import ru.rpuxa.bomjara.utils.v

class ActionImpl(
        override val id: Int,
        override val level: Int,
        override val menu: Int,
        override val name: String,
        override val addMoney: MonoCurrency,
        override val addCondition: Condition,
        override val illegal: Boolean
) : Action {


    override fun canPerform(player: Player) = when {
        !player.money.nnValue.canAdd(addMoney) -> MONEY_NEEDED
        addMoney.positive && player.condition.v.energy == 0 -> ENERGY_NEEDED
        else -> NOTHING_NEEDED
    }


    override fun perform(player: Player): Boolean {
        if (canPerform(player) != NOTHING_NEEDED)
            return false
        player.age.value = player.age.nnValue + 1
        if (addMoney.positive)
            player.addSalary(addMoney)
        else
            player.addMoney(addMoney)
        player.addCondition(addCondition)
        if (random.nextInt(40) == 10)
            player.addMoney(MonoCurrencyImpl.ONE_DIAMOND)
        if (illegal && player.daysWithoutCaught >= 2 && random.nextInt(10) == 5) {
            player.endGame.value = PlayerViewModel.CAUGHT_BY_POLICE
        } else {
            player.daysWithoutCaught++
        }

        return true
    }
}
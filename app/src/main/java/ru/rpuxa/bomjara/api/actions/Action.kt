package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.api.player.Condition
import ru.rpuxa.bomjara.api.player.Money

interface Action {

    val id: Int

    val level: Int

    val menu: Int

    val name: String

    val addMoney: Money

    val addCondition: Condition

    val illegal: Boolean
}
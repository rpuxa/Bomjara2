package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.api.actions.auxiliary.CanBuy
import ru.rpuxa.bomjara.api.actions.auxiliary.HasId
import ru.rpuxa.bomjara.api.actions.auxiliary.Nameable
import ru.rpuxa.bomjara.api.player.Money

/**
 * API for course
 */
interface Course : HasId, CanBuy, Nameable {

    /**
     * How much days you must spend to complete this course
     */
    val length: Int

    /**
     * Skip cost
     */
    val skipCost: Money
}
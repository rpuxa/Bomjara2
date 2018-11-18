package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.api.actions.auxiliary.CanBuy
import ru.rpuxa.bomjara.api.actions.auxiliary.Nameable
import ru.rpuxa.bomjara.api.player.MonoCurrency
import ru.rpuxa.bomjara.api.player.Possessions

interface ChainElement : CanBuy, Nameable {

    /**
     * Possessions that needed to buy this element
     */
    val neededPossessions: Possessions

    /**
     * Needed course id
     */
    val courseId: Int

    override val cost: MonoCurrency
}
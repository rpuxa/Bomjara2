package ru.rpuxa.bomjara.impl.actions

import ru.rpuxa.bomjara.api.actions.ChainElement
import ru.rpuxa.bomjara.api.player.MonoCurrency
import ru.rpuxa.bomjara.impl.player.PossessionsImpl

class ChainElementImpl(
        override val name: String,
        transport: Int,
        home: Int,
        friend: Int,
        location: Int,
        override val courseId: Int,
        override val cost: MonoCurrency
) : ChainElement {
    override val neededPossessions = PossessionsImpl(transport, home, friend, location)
}
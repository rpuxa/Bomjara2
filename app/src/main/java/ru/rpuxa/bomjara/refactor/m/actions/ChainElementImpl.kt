package ru.rpuxa.bomjara.refactor.m.actions

import ru.rpuxa.bomjara.api.actions.ChainElement
import ru.rpuxa.bomjara.api.player.MonoCurrency
import ru.rpuxa.bomjara.refactor.m.player.secure.SecurePossession

class ChainElementImpl(
        override val name: String,
        transport: Int,
        home: Int,
        friend: Int,
        location: Int,
        override val courseId: Int,
        override val cost: MonoCurrency
) : ChainElement {
    override val neededPossessions = SecurePossession(transport, home, friend, location)
}
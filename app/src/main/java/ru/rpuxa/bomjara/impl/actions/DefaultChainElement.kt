package ru.rpuxa.bomjara.impl.actions

import ru.rpuxa.bomjara.api.actions.ChainElement
import ru.rpuxa.bomjara.api.player.Money
import ru.rpuxa.bomjara.impl.player.DefaultPossessions

class DefaultChainElement(
        override val name: String,
        transport: Int,
        home: Int,
        friend: Int,
        location: Int,
        override val course: Int,
        override val cost: Money
) : ChainElement {
    override val neededPossessions = DefaultPossessions(transport, home, friend, location)
}
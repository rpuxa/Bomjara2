package ru.rpuxa.bomjara.impl.player

import ru.rpuxa.bomjara.api.player.MonoPossession
import ru.rpuxa.bomjara.api.player.Possessions
import ru.rpuxa.bomjara.api.player.PossessionsList

open class PossessionsImpl(
        override var transport: Int = 0,
        override var home: Int = 0,
        override var friend: Int = 0,
        override var location: Int = 0
) : Possessions {

    override fun enoughFor(standard: Possessions): MonoPossession? = when {
        transport < standard.transport -> MonoPossessionImpl(standard.transport, PossessionsList.TRANSPORT)
        home < standard.home -> MonoPossessionImpl(standard.home, PossessionsList.HOME)
        friend < standard.friend -> MonoPossessionImpl(standard.friend, PossessionsList.FRIEND)
        location < standard.location -> MonoPossessionImpl(standard.location, PossessionsList.LOCATION)
            else -> null
        }
}
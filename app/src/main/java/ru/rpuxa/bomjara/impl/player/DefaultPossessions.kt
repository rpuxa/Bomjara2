package ru.rpuxa.bomjara.impl.player

import ru.rpuxa.bomjara.api.player.MonoPossession
import ru.rpuxa.bomjara.api.player.Possessions
import ru.rpuxa.bomjara.api.player.PossessionsList

open class DefaultPossessions(
        override var transport: Int = 0,
        override var home: Int = 0,
        override var friend: Int = 0,
        override var location: Int = 0
) : Possessions {

    override fun enoughFor(standard: Possessions): MonoPossession? = when {
            transport < standard.transport -> DefaultMonoPossession(standard.transport, PossessionsList.TRANSPORT)
            home < standard.home -> DefaultMonoPossession(standard.home, PossessionsList.HOME)
            friend < standard.friend -> DefaultMonoPossession(standard.friend, PossessionsList.FRIEND)
            location < standard.location -> DefaultMonoPossession(standard.location, PossessionsList.LOCATION)
            else -> null
        }
}
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

    override fun enoughFor(standard: DefaultPossessions): MonoPossession? = when {
            transport < standard.transport -> DefaultMonoPossesion(standard.transport, PossessionsList.TRANSPORT)
            home < standard.home -> DefaultMonoPossesion(standard.home, PossessionsList.HOME)
            friend < standard.friend -> DefaultMonoPossesion(standard.friend, PossessionsList.FRIENDS)
            location < standard.location -> DefaultMonoPossesion(standard.location, PossessionsList.LOCATION)
            else -> null
        }
}
package ru.rpuxa.bomjara2.game.player

class Possessions(var transport: Int = 0, var home: Int = 0, var friend: Int = 0, var location: Int = 0) {

    fun enoughFor(standard: Possessions) = when {
        transport < standard.transport -> TRANSPORT to standard.transport
        home < standard.home -> HOUSE to standard.home
        friend < standard.friend -> FRIEND to standard.friend
        location < standard.location -> LOCATION to standard.location
        else -> NOTHING_NEEDED to NOTHING_NEEDED
    }


    companion object {
        const val NOTHING_NEEDED = 0
        const val TRANSPORT = 1
        const val HOUSE = 2
        const val FRIEND = 3
        const val LOCATION = 4
    }
}
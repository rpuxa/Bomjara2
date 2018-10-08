package ru.rpuxa.bomjara2.game.player

class Possessions(var transport: Int = 0, var house: Int = 0, var friend: Int = 0, var location: Int = 0) {

    fun enoughFor(standard: Possessions) = when {
        transport < standard.transport -> TRANSPORT to transport
        house < standard.house -> HOUSE to house
        friend < standard.friend -> FRIEND to friend
        location < standard.location -> LOCATION to location
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
package ru.rpuxa.bomjara2.game.player

class Possessions(var transport: Int = 0, var house: Int = 0, var friend: Int = 0, var location: Int = 0) {

    fun enoughtFor(standard: Possessions) =
            transport >= standard.transport && house >= standard.house && friend >= standard.friend && location >= standard.location
}
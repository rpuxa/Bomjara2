package ru.rpuxa.bomjara.api.player

import ru.rpuxa.bomjara.game.player.Possessions


interface Possessions {

    var transport: Int

    var home: Int

    var friend: Int

    var location: Int

    fun enoughFor(standard: Possessions): MonoPossession?
}
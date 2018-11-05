package ru.rpuxa.bomjara.api.player

import ru.rpuxa.bomjara.impl.player.DefaultPossessions


interface Possessions {

    var transport: Int

    var home: Int

    var friend: Int

    var location: Int

    fun enoughFor(standard: DefaultPossessions): MonoPossession?
}
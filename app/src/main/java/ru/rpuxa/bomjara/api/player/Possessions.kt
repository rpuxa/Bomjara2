package ru.rpuxa.bomjara.api.player


interface Possessions {

    var transport: Int

    var home: Int

    var friend: Int

    var location: Int

    fun enoughFor(standard: Possessions): MonoPossession?
}
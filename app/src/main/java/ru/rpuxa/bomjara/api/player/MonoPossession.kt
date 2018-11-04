package ru.rpuxa.bomjara.api.player

interface MonoPossession : Possessions {
    val value: Int

    val possession: PossessionsList
}
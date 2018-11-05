package ru.rpuxa.bomjara.impl.player

import ru.rpuxa.bomjara.api.player.MonoPossession
import ru.rpuxa.bomjara.api.player.PossessionsList

class DefaultMonoPossesion : DefaultPossessions, MonoPossession {
    override var value
        get() = when (possession) {
        PossessionsList.LOCATION -> location
        PossessionsList.FRIENDS -> friend
        PossessionsList.TRANSPORT -> transport
        PossessionsList.HOME -> home
    }
    set(value) {
        when (possession) {
            PossessionsList.LOCATION -> location = value
            PossessionsList.FRIENDS -> friend = value
            PossessionsList.TRANSPORT -> transport = value
            PossessionsList.HOME -> home = value
        }
    }

    override val possession: PossessionsList

    constructor(value: Int, possession: PossessionsList) : super() {
        this.possession = possession
        this.value = value
    }
}
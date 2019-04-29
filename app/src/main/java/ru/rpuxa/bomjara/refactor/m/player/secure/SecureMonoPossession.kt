package ru.rpuxa.bomjara.refactor.m.player.secure

import ru.rpuxa.bomjara.api.player.MonoPossession
import ru.rpuxa.bomjara.api.player.PossessionsList

class SecureMonoPossession(value: Int, override val possession: PossessionsList) : SecurePossession(), MonoPossession {

    override var value
        get() = when (possession) {
            PossessionsList.LOCATION -> location
            PossessionsList.FRIEND -> friend
            PossessionsList.TRANSPORT -> transport
            PossessionsList.HOME -> home
        }
        set(value) {
            when (possession) {
                PossessionsList.LOCATION -> location = value
                PossessionsList.FRIEND -> friend = value
                PossessionsList.TRANSPORT -> transport = value
                PossessionsList.HOME -> home = value
            }
        }

    init {
        this.value = value
    }
}
package ru.rpuxa.bomjara.refactor.m.player.secure

import ru.rpuxa.bomjara.api.player.MonoPossession
import ru.rpuxa.bomjara.api.player.Possessions
import ru.rpuxa.bomjara.api.player.PossessionsList
import ru.rpuxa.bomjara.refactor.m.secure.SecureInt

open class SecurePossession(
        transport: Int,
        home: Int,
        friend: Int,
        location: Int
) : Possessions {

    constructor() : this(0, 0, 0, 0)

    private val _transport = SecureInt(transport)
    private val _home = SecureInt(home)
    private val _friend = SecureInt(friend)
    private val _location = SecureInt(location)

    override var transport
        get() = _transport.value
        set(value) {
            _transport.value = value
        }
    override var home: Int
        get() = _home.value
        set(value) {
            _home.value = value
        }
    override var friend: Int
        get() = _friend.value
        set(value) {
            _friend.value = value
        }
    override var location: Int
        get() = _location.value
        set(value) {
            _location.value = value
        }

    override fun enoughFor(standard: Possessions): MonoPossession? = when {
        transport < standard.transport -> SecureMonoPossesion(standard.transport, PossessionsList.TRANSPORT)
        home < standard.home -> SecureMonoPossesion(standard.home, PossessionsList.HOME)
        friend < standard.friend -> SecureMonoPossesion(standard.friend, PossessionsList.FRIEND)
        location < standard.location -> SecureMonoPossesion(standard.location, PossessionsList.LOCATION)
        else -> null
    }
}
package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.actions.Action
import ru.rpuxa.bomjara.actions.ChainElement
import ru.rpuxa.bomjara.actions.Course

interface ActionsBase {

    val actions: Array<Action>

    val locations: Array<ChainElement>

    val friends: Array<ChainElement>

    val transports: Array<ChainElement>

    val homes: Array<ChainElement>

    val courses: Array<Course>

    val vips: Array<Vip>

    fun getPenalty(player: Player)
}
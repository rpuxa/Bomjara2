package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.api.player.Money
import ru.rpuxa.bomjara.api.player.Possessions

interface ChainElement {
    val name: String

    val neededPossessions: Possessions

    val course: CourseId

    val cost: Money
}
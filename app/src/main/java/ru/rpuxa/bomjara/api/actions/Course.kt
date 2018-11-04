package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.api.player.Money

typealias CourseId = Int

interface Course {

    val id: CourseId

    val name: String

    val cost: Money

    val length: Int

    val skipCost: Money
}
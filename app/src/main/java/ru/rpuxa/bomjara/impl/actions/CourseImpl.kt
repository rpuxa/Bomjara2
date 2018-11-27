package ru.rpuxa.bomjara.impl.actions

import ru.rpuxa.bomjara.api.actions.Course
import ru.rpuxa.bomjara.api.player.MonoCurrency

class CourseImpl(
        override val id: Int,
        override val name: String,
        override val cost: MonoCurrency,
        override val length: Int
) : Course {
    override val skipCost = cost.multiply(4.0)
}

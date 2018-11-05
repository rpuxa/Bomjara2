package ru.rpuxa.bomjara.impl.actions

import ru.rpuxa.bomjara.api.actions.Course
import ru.rpuxa.bomjara.api.actions.CourseId
import ru.rpuxa.bomjara.api.player.MonoCurrency

class DefaultCourse(
        override val id: CourseId,
        override val name: String,
        override val cost: MonoCurrency,
        override val length: Int
) : Course {
    override val skipCost = cost.multiply(4.0)
}

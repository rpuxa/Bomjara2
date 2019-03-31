package ru.rpuxa.bomjara.utils

import kotlin.math.abs


val gauss: Double
    get() {
        var gauss = 4.0
        while (abs(gauss) > 3.0)
            gauss = random.nextGaussian()
        gauss /= 6.0
        return gauss + 1.0
    }

fun ageToString(age: Int) =
        "${25 + age / 365} лет ${age % 365} дней"

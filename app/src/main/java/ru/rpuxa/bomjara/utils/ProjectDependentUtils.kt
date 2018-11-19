package ru.rpuxa.bomjara.utils

import android.content.Context
import android.support.v4.app.Fragment
import ru.rpuxa.bomjara.impl.Data
import kotlin.math.abs


val gauss: Double
    get() {
        var gauss = 4.0
        while (abs(gauss) > 3.0)
            gauss = random.nextGaussian()
        gauss /= 6.0
        return gauss + 1.0
    }

fun Context.save() {
    val file = filesDir
    Data.saveLoader.savePlayer(Data.player)
    Data.saveLoader.saveToFile(file)
    Data.settings.saveToFile(file)
    Data.statistic.saveToFile(file)
    Data.actionsBase.save(file)
}

fun Fragment.save() {
    context!!.save()
}

fun ageToString(age: Int) =
        "${25 + age / 365} лет ${age % 365} дней"
package ru.rpuxa.bomjara.utils

import android.content.Context
import android.support.v4.app.Fragment
import ru.rpuxa.bomjara.CurrentData
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
    CurrentData.saveLoader.savePlayer(CurrentData.player)
    CurrentData.saveLoader.saveToFile(file, this)
    CurrentData.settings.saveToFile(file)
    CurrentData.statistic.saveToFile(file, this)
    CurrentData.actionsBase.save(file)
}

fun Fragment.save() {
    context!!.save()
}

fun ageToString(age: Int) =
        "${25 + age / 365} лет ${age % 365} дней"

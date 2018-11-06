package ru.rpuxa.bomjara.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.actions.ActionsMenus
import ru.rpuxa.bomjara.api.player.Currencies
import ru.rpuxa.bomjara.api.player.MonoCurrency
import ru.rpuxa.bomjara.impl.Data.actionsBase
import ru.rpuxa.bomjara.impl.Data.player
import ru.rpuxa.bomjara.impl.Data.saveLoader
import ru.rpuxa.bomjara.impl.Data.settings
import ru.rpuxa.bomjara.impl.Data.statistic
import java.io.*
import java.util.*
import kotlin.math.abs


fun Context.getCurrencyIcon(money: MonoCurrency) =
        BitmapFactory.decodeResource(
                resources,
                when (money.currency) {
                    Currencies.RUBLES -> R.drawable.ruble
                    Currencies.EUROS -> R.drawable.euro
                    Currencies.BITCOINS -> R.drawable.bitcoin
                    else -> R.drawable.bottle
                }
        )!!

fun Context.getMenuIcon(menu: Int) =
        BitmapFactory.decodeResource(
                resources,
                when (menu) {
                    ActionsMenus.ENERGY.id -> R.drawable.colored_energy
                    ActionsMenus.FOOD.id -> R.drawable.colored_food
                    ActionsMenus.HEALTH.id -> R.drawable.colored_health
                    else -> R.drawable.colored_job
                }
        )!!

fun Activity.toast(msg: String, isShort: Boolean = true) {
    runOnUiThread {
        Toast.makeText(this, msg, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
    }
}

fun Fragment.toast(msg: String, isShort: Boolean = true) =
        activity.toast(msg, isShort)

fun Activity.toast(@StringRes id: Int, isShort: Boolean = true) = toast(getString(id), isShort)

fun Fragment.toast(@StringRes id: Int, isShort: Boolean = true) = toast(getString(id), isShort)

fun Long.divider(): String {
    var s = toString()
    var newString = ""
    while (s.length > 3) {
        newString = " " + s.substring(s.length - 3) + newString
        s = s.substring(0, s.length - 3)
    }

    return s + newString
}


val random = Random()

val gauss: Double
    get() {
        var gauss = 4.0
        while (abs(gauss) > 3.0)
            gauss = random.nextGaussian()
        gauss /= 6.0
        return gauss + 1.0
    }

fun Long.roundTimes(i: Double) = Math.ceil(this * i).toLong()

fun changeVisibility(visibility: Int, vararg views: View) {
    views.forEach { it.visibility = visibility }
}

fun Context.save() {
    val file = filesDir
    saveLoader.savePlayer(player)
    saveLoader.saveToFile(file)
    settings.saveToFile(file)
    statistic.saveToFile(file)
    actionsBase.save(file)
}

inline fun <reified T : Activity> Activity.startActivity() =
        startActivity(Intent(this, T::class.java))


inline fun <reified T : Activity> Activity.startActivity(enterAnim: Int, exitAnim: Int) {
    startActivity<T>()
    overridePendingTransition(enterAnim, exitAnim)
}

inline fun <reified T : Activity> Activity.startActivityFromRight() {
    startActivity<T>(R.anim.right_in, R.anim.left_out)
}


fun getStringAge(i: Int) =
        "${25 + i / 365} лет ${i % 365} дней"

fun File.writeObject(obj: Any, fileName: String) {
    ObjectOutputStream(FileOutputStream(File(this, fileName))).use {
        it.writeObject(obj)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> File.readObject(fileName: String?): T? {
    return try {
        ObjectInputStream(FileInputStream(if (fileName == null) this else File(this, fileName))).use {
            it.readObject() as? T
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Context.browser(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

fun Context.browser(@StringRes res: Int) = browser(getString(res))

val MONTHS = arrayOf(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
)

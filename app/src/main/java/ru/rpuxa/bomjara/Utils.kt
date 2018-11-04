package ru.rpuxa.bomjara

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.actions.Actions.ENERGY
import ru.rpuxa.bomjara.actions.Actions.FOOD
import ru.rpuxa.bomjara.actions.Actions.HEALTH
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.game.player.Money
import ru.rpuxa.bomjara.save.SaveLoader
import ru.rpuxa.bomjara.server.Server
import ru.rpuxa.bomjara.settings.saveSettings
import ru.rpuxa.bomjara.statistic.Statistic
import java.io.*
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.abs


inline val Int.rub get() = Money(rubles = toLong())
inline val Int.euro get() = Money(euros = toLong())
inline val Int.bitcoin get() = Money(bitcoins = toLong())
inline val Int.bottle get() = Money(bottles = toLong())

fun Long.currency(id: Int) = when (id) {
    RUB -> Money(rubles = this)
    EURO -> Money(euros = this)
    BITCOIN -> Money(bitcoins = this)
    BOTTLES -> Money(bottles = this)
    else -> throw IllegalStateException()
}

const val NONE = -1488
const val RUB = 0
const val EURO = 1
const val BITCOIN = 2
const val BOTTLES = 3
const val DIAMONDS = 4

fun Context.getCurrencyIcon(money: Money) =
        BitmapFactory.decodeResource(
                resources,
                when (money.currency) {
                    RUB -> R.drawable.ruble
                    EURO -> R.drawable.euro
                    BITCOIN -> R.drawable.bitcoin
                    else -> R.drawable.bottle
                }
        )!!

fun Context.getMenuIcon(menu: Int) =
        BitmapFactory.decodeResource(
                resources,
                when (menu) {
                    ENERGY -> R.drawable.colored_energy
                    FOOD -> R.drawable.colored_food
                    HEALTH -> R.drawable.colored_health
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
    SaveLoader.savePlayer(Player.current)
    SaveLoader.save(file)
    saveSettings(file)
    Statistic.save(file)
    Actions.save(file)
}

fun Context.load() {
    val file = filesDir
    Actions.load(file, assets.open("actions.bomj"))
    SaveLoader.load(file)
    Statistic.load(file, packageManager.getPackageInfo(this.packageName, 0).versionCode)
    Actions.loadFromServer()
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

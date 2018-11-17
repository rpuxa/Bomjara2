package ru.rpuxa.bomjara.utils

import android.content.Context
import ru.rpuxa.bomjara.impl.Data.actionsBase
import ru.rpuxa.bomjara.impl.Data.player
import ru.rpuxa.bomjara.impl.Data.saveLoader
import ru.rpuxa.bomjara.impl.Data.settings
import ru.rpuxa.bomjara.impl.Data.statistic
import java.io.*
import java.util.*
import kotlin.math.abs



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

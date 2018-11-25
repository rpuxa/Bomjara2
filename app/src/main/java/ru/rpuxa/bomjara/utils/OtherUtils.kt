package ru.rpuxa.bomjara.utils

import android.util.Log
import java.io.*
import java.util.*



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
        val obj = ObjectInputStream(FileInputStream(if (fileName == null) this else File(this, fileName))).use {
            it.readObject() as? T
        }
        Log.e("suka", "all loaded => $fileName    $obj")

        obj
    } catch (e: Exception) {
        Log.e("suka", "$fileName  ${e.toString()}")
        e.printStackTrace()
        null
    }
}

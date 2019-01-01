package ru.rpuxa.bomjara.cache

import android.content.Context
import java.io.File

interface Savable {

    fun saveToFile(filesDir: File, context: Context)

    fun loadFromFile(filesDir: File, context: Context)
}
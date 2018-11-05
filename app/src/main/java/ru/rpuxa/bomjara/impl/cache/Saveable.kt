package ru.rpuxa.bomjara.impl.cache

import java.io.File

interface Saveable {

    fun saveToFile(filesDir: File)

    fun loadFromFile(filesDir: File)
}
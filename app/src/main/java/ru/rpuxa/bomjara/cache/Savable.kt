package ru.rpuxa.bomjara.cache

import java.io.File

interface Savable {

    fun saveToFile(filesDir: File)

    fun loadFromFile(filesDir: File)
}
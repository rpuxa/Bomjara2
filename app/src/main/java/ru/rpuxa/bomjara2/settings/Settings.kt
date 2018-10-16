package ru.rpuxa.bomjara2.settings

import ru.rpuxa.bomjara2.readObject
import ru.rpuxa.bomjara2.writeObject
import java.io.File
import java.io.Serializable

const val SETTINGS_FILE_NAME = "settings2.0"

object Settings : ISettings by settings

private lateinit var settings: CacheSettings

fun loadSettings(file: File) {
    settings = file.readObject<CacheSettings>(SETTINGS_FILE_NAME) ?: CacheSettings()
}

fun saveSettings(file: File) {
    file.writeObject(settings, SETTINGS_FILE_NAME)
}

class CacheSettings : ISettings {
    override var showTips = true
    override var lastSave = -1488228L
}

interface ISettings : Serializable {
    var showTips: Boolean
    var lastSave: Long
}
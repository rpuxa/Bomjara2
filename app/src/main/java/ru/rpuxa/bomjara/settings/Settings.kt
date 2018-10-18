package ru.rpuxa.bomjara.settings

import ru.rpuxa.bomjara.writeObject
import java.io.File

const val SETTINGS_FILE_NAME = "settings2.0"

object Settings : ISettings by settings

private lateinit var settings: CacheSettings

fun loadSettings(file: File) {
    settings = ru.rpuxa.bomjara.cache.SuperDeserializator.deserialize(file, SETTINGS_FILE_NAME) as? CacheSettings? ?: CacheSettings()
}

fun saveSettings(file: File) {
    file.writeObject(settings.serialize(), SETTINGS_FILE_NAME)
}

class CacheSettings : ISettings {
    override var showTips = true
    override var lastSave = -1488228L
}

interface ISettings : ru.rpuxa.bomjara.cache.SuperSerializable {
    var showTips: Boolean
    var lastSave: Long
}
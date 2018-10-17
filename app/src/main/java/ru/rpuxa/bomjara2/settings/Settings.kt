package ru.rpuxa.bomjara2.settings

import ru.rpuxa.bomjara2.cache.SuperDeserializator
import ru.rpuxa.bomjara2.cache.SuperSerializable
import ru.rpuxa.bomjara2.writeObject
import java.io.File

const val SETTINGS_FILE_NAME = "settings2.0"

object Settings : ISettings by settings

private lateinit var settings: CacheSettings

fun loadSettings(file: File) {
    settings = SuperDeserializator.deserialize(file, SETTINGS_FILE_NAME) as? CacheSettings? ?: CacheSettings()
}

fun saveSettings(file: File) {
    file.writeObject(settings.serialize(), SETTINGS_FILE_NAME)
}

class CacheSettings : ISettings {
    override var showTips = true
    override var lastSave = -1488228L
}

interface ISettings : SuperSerializable {
    var showTips: Boolean
    var lastSave: Long
}
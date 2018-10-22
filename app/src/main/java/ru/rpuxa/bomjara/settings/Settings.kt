package ru.rpuxa.bomjara.settings

import ru.rpuxa.bomjara.activities.App
import ru.rpuxa.bomjara.cache.SuperDeserializator
import ru.rpuxa.bomjara.cache.SuperSerializable
import ru.rpuxa.bomjara.writeObject
import java.io.File

const val SETTINGS_FILE_NAME = "settings2.0"

val Settings: ISettings by lazy {
    SuperDeserializator.deserialize(App.files, SETTINGS_FILE_NAME) as? CacheSettings?
            ?: CacheSettings()
}


fun saveSettings(file: File) {
    Thread {
        file.writeObject(Settings.serialize(), SETTINGS_FILE_NAME)

    }.start()
}

class CacheSettings : ISettings {
    override var showTips = true
    override var lastSave = -1488228L
}

interface ISettings : SuperSerializable {
    var showTips: Boolean
    var lastSave: Long
}
package ru.rpuxa.bomjara.impl.settings

import ru.rpuxa.bomjara.api.settings.Settings
import ru.rpuxa.bomjara.cache.SuperSerializable
import ru.rpuxa.bomjara.utils.writeObject
import java.io.File

class DefaultSettings : Settings, SuperSerializable {
    override var showTips = true
    override var lastSave = -148822891823
    override var wasRated = false

    override fun saveToFile(filesDir: File) {
        filesDir.writeObject(serialize(), SETTINGS_FILE_NAME)
    }

    companion object {
        const val SETTINGS_FILE_NAME = "settings2.0"
    }
}
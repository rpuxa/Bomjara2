package ru.rpuxa.bomjara.impl.settings

import ru.rpuxa.bomjara.api.settings.Settings
import ru.rpuxa.bomjara.impl.cache.SuperSerializable
import ru.rpuxa.bomjara.impl.writeObject
import java.io.File

class DefaultSettings : Settings, SuperSerializable {
    override var showTips = true
    override var lastSave = -148822891823
    override var showVkGroupInvite = true
    override var wasRated = false

    override fun saveToFile(filesDir: File) {
        filesDir.writeObject(serialize(), SETTINGS_FILE_NAME)
    }

    companion object {
        const val SETTINGS_FILE_NAME = "settings2.0"
    }
}
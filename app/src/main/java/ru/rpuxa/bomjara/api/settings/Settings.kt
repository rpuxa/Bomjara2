package ru.rpuxa.bomjara.api.settings

import java.io.File

interface Settings {

    var showTips: Boolean

    var lastSave: Long

    var showVkGroupInvite: Boolean

    var wasRated: Boolean

    fun saveToFile(filesDir: File)
}
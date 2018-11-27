package ru.rpuxa.bomjara.impl.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.startActivity
import ru.rpuxa.bomjara.CurrentData.saveLoader
import ru.rpuxa.bomjara.CurrentData.statistic
import ru.rpuxa.bomjara.CurrentMutableData
import ru.rpuxa.bomjara.api.settings.Settings
import ru.rpuxa.bomjara.cache.SuperDeserializator
import ru.rpuxa.bomjara.impl.data.DataImpl.actionsBase
import ru.rpuxa.bomjara.impl.settings.SettingsImpl
import ru.rpuxa.bomjara.impl.settings.SettingsImpl.Companion.SETTINGS_FILE_NAME

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        load()
        startActivity<MenuActivity>()
    }

    private fun load() {
        val file = filesDir
        actionsBase.load(file, assets.open("actions.bomj"))
        statistic.loadFromFile(file)
        CurrentMutableData.settings = SuperDeserializator.deserialize(file, SETTINGS_FILE_NAME) as? Settings? ?: SettingsImpl()
        saveLoader.loadFromFile(file)
    }
}

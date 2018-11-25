package ru.rpuxa.bomjara.impl.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.startActivity
import ru.rpuxa.bomjara.api.settings.Settings
import ru.rpuxa.bomjara.cache.SuperDeserializator
import ru.rpuxa.bomjara.impl.Data
import ru.rpuxa.bomjara.impl.Data.saveLoader
import ru.rpuxa.bomjara.impl.Data.statistic
import ru.rpuxa.bomjara.impl.settings.DefaultSettings
import ru.rpuxa.bomjara.impl.settings.DefaultSettings.Companion.SETTINGS_FILE_NAME

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        load()
        startActivity<MenuActivity>()
    }

    private fun load() {
        val file = filesDir
        Data.actionsBase.load(file, assets.open("actions.bomj"))
        statistic.versionCode = packageManager.getPackageInfo(this.packageName, 0).versionCode
        statistic.loadFromFile(file)
        Data.settings = SuperDeserializator.deserialize(file, SETTINGS_FILE_NAME) as? Settings? ?: DefaultSettings()
        saveLoader.loadFromFile(file)
    }
}

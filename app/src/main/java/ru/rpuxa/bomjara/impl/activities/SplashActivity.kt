package ru.rpuxa.bomjara.impl.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.rpuxa.bomjara.api.settings.Settings
import ru.rpuxa.bomjara.impl.cache.SuperDeserializator
import ru.rpuxa.bomjara.impl.Data
import ru.rpuxa.bomjara.impl.settings.DefaultSettings
import ru.rpuxa.bomjara.settings.SETTINGS_FILE_NAME
import ru.rpuxa.bomjara.impl.startActivity
import ru.rpuxa.bomjara.impl.statistic.DefaultStatistic

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        load()
        startActivity<MenuActivity>()
    }

    private fun load() {
        val file = filesDir
        Data.actionsBase.load(file, assets.open("actions.bomj"))
        DefaultStatistic.load(file, packageManager.getPackageInfo(this.packageName, 0).versionCode)
        Data.settings = SuperDeserializator.deserialize(file, SETTINGS_FILE_NAME) as? Settings? ?: DefaultSettings()
    }
}

package ru.rpuxa.bomjara.refactor.v

import android.annotation.SuppressLint
import android.app.Application
import com.google.android.gms.ads.MobileAds
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.settings.Settings
import ru.rpuxa.bomjara.cache.SuperDeserializator
import ru.rpuxa.bomjara.impl.save.StringSaveLoader
import ru.rpuxa.bomjara.impl.settings.SettingsImpl
import ru.rpuxa.bomjara.refactor.dagger.Component
import ru.rpuxa.bomjara.refactor.dagger.DaggerComponent
import ru.rpuxa.bomjara.refactor.dagger.Provider
import ru.rpuxa.bomjara.refactor.m.ActionsLoader
import ru.rpuxa.bomjara.refactor.m.MyDataBase
import java.io.File
import javax.inject.Inject

class Bomjara : Application() {
    @Inject
    lateinit var action: ActionsLoader
    @Inject
    lateinit var myDataBase: MyDataBase


    override fun onCreate() {
        super.onCreate()
        component = DaggerComponent.builder()
                .provider(Provider(this))
                .build()
        component.inject(this)
        load()
    }

    private fun load() {
        MobileAds.initialize(this, getString(R.string.ad_id))
        videoAd = Ad(this, getString(R.string.dead_banner_id))
        action.load()


        //<editor-fold desc="Legacy code">
        (SuperDeserializator.deserialize(filesDir, SettingsImpl.SETTINGS_FILE_NAME) as? Settings?)?.let { settings ->
            myDataBase.apply {
                    setLastSaveId(settings.lastSave)
                    setShowTips(settings.showTips)
                }
            File(filesDir, SettingsImpl.SETTINGS_FILE_NAME).delete()
        }

        StringSaveLoader.loadFromFile(this)?.forEach {
            myDataBase.updatePlayer(
                    it.id,
                    it.name,
                    it.age,
                    it.bottles,
                    it.rubles + it.euros * 100 + it.bitcoins * 3000,
                    it.diamonds,
                    it.location,
                    it.friend,
                    it.home,
                    it.transport,
                    it.efficiency,
                    it.maxEnergy,
                    it.maxFullness,
                    it.maxHealth,
                    it.maxEnergy,
                    it.maxFullness,
                    it.maxHealth,
                    it.courses,
                    deadByHungry = false,
                    deadByZeroHealth = false,
                    caughtByPolice = false
            )
        }
        //</editor-fold>
    }

    companion object {
        lateinit var component: Component
            private set
        @SuppressLint("StaticFieldLeak")
        lateinit var videoAd: Ad
            private set
    }
}
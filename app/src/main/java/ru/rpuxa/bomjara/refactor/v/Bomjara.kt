package ru.rpuxa.bomjara.refactor.v

import android.annotation.SuppressLint
import android.app.Application
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.impl.save.StringSaveLoader
import ru.rpuxa.bomjara.refactor.dagger.Component
import ru.rpuxa.bomjara.refactor.dagger.DaggerComponent
import ru.rpuxa.bomjara.refactor.dagger.Provider
import ru.rpuxa.bomjara.refactor.m.ActionsLoader
import ru.rpuxa.bomjara.refactor.m.MyDataBase
import ru.rpuxa.bomjara.refactor.m.MyDataBase.Save.Companion.ALIVE
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
        File(filesDir, "settings2.0").delete()

        val oldSaves = StringSaveLoader.loadFromFile(this)

        if (oldSaves != null)
            runBlocking(Dispatchers.IO) {
                for (it in oldSaves) {
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
                            ALIVE
                    )
                }

                myDataBase.setLastSaveId(oldSaves.maxBy { it.age }?.id ?: return@runBlocking)
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
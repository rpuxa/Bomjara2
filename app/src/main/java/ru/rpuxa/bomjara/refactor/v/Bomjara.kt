package ru.rpuxa.bomjara.refactor.v

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
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
import kotlin.math.min

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

        registerInGameTime()
    }

    private fun registerInGameTime() {
        val listener = object : View.OnTouchListener {

            private var lastTouchTime = System.nanoTime()

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                inGameTime += min( System.nanoTime() - lastTouchTime, MAX_TIME_WITHOUT_TOUCH)

                return true
            }
        }
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                activity.window.decorView.setOnTouchListener(null)
            }

            override fun onActivityResumed(activity: Activity) {
                activity.window.decorView.setOnTouchListener(listener)
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            }
        })
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

        private const val MAX_TIME_WITHOUT_TOUCH = 5_000_000_000L //5 sec

        var inGameTime = 0L
            private set
    }
}
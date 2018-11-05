package ru.rpuxa.bomjara.impl.activities

import android.app.Application
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.impl.Ad

class App : Application() {
    lateinit var videoAd: Ad


    override fun onCreate() {
        super.onCreate()
        videoAd = Ad(this, getString(R.string.dead_banner_id))
    }

}
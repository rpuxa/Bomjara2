package ru.rpuxa.bomjara.activities

import android.app.Application
import ru.rpuxa.bomjara.Ad
import ru.rpuxa.bomjara.R

class App : Application() {
    lateinit var videoAd: Ad


    override fun onCreate() {
        super.onCreate()
        videoAd = Ad(this, getString(R.string.dead_banner_id))
    }


}
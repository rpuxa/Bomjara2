package ru.rpuxa.bomjara2.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.settings.loadSettings
import ru.rpuxa.bomjara2.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        load()
        startActivity<MenuActivity>()
    }

    private fun load() {
        loadSettings(filesDir)
        MobileAds.initialize(this, getString(R.string.ad_id))
    }
}

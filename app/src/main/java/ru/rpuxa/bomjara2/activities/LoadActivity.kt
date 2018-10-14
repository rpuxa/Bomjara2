package ru.rpuxa.bomjara2.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import ru.rpuxa.bomjara2.R

class LoadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        load()
        startActivity(Intent(this, ContentActivity::class.java))
    }

    private fun load() {
        MobileAds.initialize(this, getString(R.string.ad_id))
    }
}

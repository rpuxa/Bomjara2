package ru.rpuxa.bomjara.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.rpuxa.bomjara.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity<MenuActivity>()
    }
}

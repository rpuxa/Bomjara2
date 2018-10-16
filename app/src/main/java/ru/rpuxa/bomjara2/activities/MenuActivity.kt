package ru.rpuxa.bomjara2.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_menu.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.save.SaveLoader

class MenuActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SaveLoader.load(filesDir)
        setContentView(R.layout.main_menu)
        saves.setOnClickListener {
            startActivity(Intent(this, SavesActivity::class.java))
        }

        quit.setOnClickListener {
            System.exit(0)
        }
    }

    override fun onBackPressed() {
    }
}

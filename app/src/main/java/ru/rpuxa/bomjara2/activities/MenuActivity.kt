package ru.rpuxa.bomjara2.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.menu.*
import ru.rpuxa.bomjara2.MenuFragmentAdapter
import ru.rpuxa.bomjara2.R

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        //pager.adapter = MenuFragmentAdapter(supportFragmentManager).openSaves()

    }
}

package ru.rpuxa.bomjara2.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content.*
import ru.rpuxa.bomjara2.ContentAdapter
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.R.drawable

class ContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content)

        pager.adapter = ContentAdapter(supportFragmentManager)
        scroll_buttons.setIcons(drawable.energy_menu, drawable.food_menu, drawable.health_menu)
        scroll_buttons.setViewPager(pager)
    }
}
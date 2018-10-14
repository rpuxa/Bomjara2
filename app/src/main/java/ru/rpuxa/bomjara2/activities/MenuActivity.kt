package ru.rpuxa.bomjara2.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.R.drawable
import ru.rpuxa.bomjara2.save.SaveLoader

class MenuActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SaveLoader.load(filesDir)
        setContentView(R.layout.content)
        scroll_buttons.setIcons(drawable.info, drawable.friend)
        scroll_buttons.setViewPager(pager)
    }

}

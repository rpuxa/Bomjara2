package ru.rpuxa.bomjara2.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.R.drawable

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content)
        scroll_buttons.setIcons(drawable.info, drawable.friend)
        pager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            val ar = arrayOf(
                    H1(), H2()

            )

            override fun getItem(position: Int): Fragment {
                return ar[position]
            }

            override fun getCount() = ar.size
        }

        scroll_buttons.setViewPager(pager)
    }

    class H1 : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.h1, container, false)
        }
    }

    class H2 : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.h2, container, false)
        }
    }

}

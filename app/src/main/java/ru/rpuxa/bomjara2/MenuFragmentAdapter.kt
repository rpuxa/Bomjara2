package ru.rpuxa.bomjara2

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.rpuxa.bomjara2.fragments.MainMenuFragment

class MenuFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragments = arrayOf<Fragment>(
            MainMenuFragment()
    )

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}
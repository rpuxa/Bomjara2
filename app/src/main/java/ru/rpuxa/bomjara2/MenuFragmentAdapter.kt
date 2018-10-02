package ru.rpuxa.bomjara2

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.rpuxa.bomjara2.fragments.MainMenuFragment
import ru.rpuxa.bomjara2.fragments.SavesListFragment

class MenuFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
 //   private val menu =
    private lateinit var fragments: Array<out Fragment>

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    fun openSaves(): MenuFragmentAdapter {
        addFragment(SavesListFragment())
        return this
    }

    private fun addFragment(fragment: Fragment) {
        fragments = arrayOf(MainMenuFragment())
    }

}
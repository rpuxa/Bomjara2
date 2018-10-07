package ru.rpuxa.bomjara2

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.fragments.ActionsListFragment

class ContentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf(
            ActionsListFragment.create(Actions.ENERGY),
            ActionsListFragment.create(Actions.FOOD),
            ActionsListFragment.create(Actions.HEALTH)
    )

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}
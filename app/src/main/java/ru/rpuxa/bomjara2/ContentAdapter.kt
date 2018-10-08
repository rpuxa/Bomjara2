package ru.rpuxa.bomjara2

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.fragments.ActionsListFragment
import ru.rpuxa.bomjara2.fragments.InfoFragment
import ru.rpuxa.bomjara2.fragments.LocationFragment

class ContentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf(
            InfoFragment(),
            LocationFragment(),
            ActionsListFragment.create(Actions.ENERGY),
            ActionsListFragment.create(Actions.FOOD),
            ActionsListFragment.create(Actions.HEALTH),
            ActionsListFragment.create(Actions.JOBS)
    )

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}
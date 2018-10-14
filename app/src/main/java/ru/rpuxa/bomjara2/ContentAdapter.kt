package ru.rpuxa.bomjara2

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.fragments.*

class ContentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf(
            InfoFragment(),
            ExchangeFragment(),
            LocationFragment(),
            TransportChain(),
            CoursesFragment(),
            ActionsListFragment.create(Actions.ENERGY),
            ActionsListFragment.create(Actions.FOOD),
            ActionsListFragment.create(Actions.HEALTH),
            ActionsListFragment.create(Actions.JOBS),
            VipFragment()
    )

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}
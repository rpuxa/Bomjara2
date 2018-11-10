package ru.rpuxa.bomjara.impl

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.rpuxa.bomjara.api.actions.ActionsMenus
import ru.rpuxa.bomjara.impl.fragments.*

class ContentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf(
            InfoFragment(),
            ExchangeFragment(),
            LocationFragment(),
            TransportChain(),
            CoursesFragment(),
            ActionsListFragment.create(ActionsMenus.ENERGY.id),
            ActionsListFragment.create(ActionsMenus.FOOD.id),
            ActionsListFragment.create(ActionsMenus.HEALTH.id),
            ActionsListFragment.create(ActionsMenus.JOBS.id),
            VipFragment()
           // MediaFragment()
    )

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}
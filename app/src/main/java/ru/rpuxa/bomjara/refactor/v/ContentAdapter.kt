package ru.rpuxa.bomjara.refactor.v

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.rpuxa.bomjara.api.actions.ActionsMenus
import ru.rpuxa.bomjara.refactor.v.fragments.*

class ContentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf(
            InfoFragment(),
            ExchangeFragment(),
            LocationFragment(),
            TransportFragment(),
            CoursesFragment(),
            ActionsListFragment.create(ActionsMenus.ENERGY.id),
            ActionsListFragment.create(ActionsMenus.FOOD.id),
            ActionsListFragment.create(ActionsMenus.HEALTH.id),
            ActionsListFragment.create(ActionsMenus.JOBS.id),
            VipFragment()
    )

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}
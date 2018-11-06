package ru.rpuxa.bomjara.impl.fragments

import android.view.View
import kotlinx.android.synthetic.main.chain.view.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.api.actions.ChainElement
import ru.rpuxa.bomjara.api.player.PossessionsList
import ru.rpuxa.bomjara.impl.Data
import ru.rpuxa.bomjara.impl.Data.actionsBase
import ru.rpuxa.bomjara.impl.Data.player
import ru.rpuxa.bomjara.impl.changeVisibility
import ru.rpuxa.bomjara.impl.getCurrencyIcon
import ru.rpuxa.bomjara.impl.toast
import kotlin.reflect.KMutableProperty0

abstract class ChainFragment : CacheFragment() {

    protected fun View.install(name0: String,
                               icon0: Int,
                               changeText: String,
                               elements: Array<ChainElement>,
                               ref: KMutableProperty0<Int>) {
        if (ref.get() + 1 >= elements.size) {
            changeVisibility(View.GONE, next, change, change_label, cost, currency)
            return
        }
        val element = elements[ref.get() + 1]
        val possessions = element.neededPossessions
        val money = element.cost.inv()
        val course = element.course
        name.text = name0
        icon.setImageResource(icon0)
        current.text = elements[ref.get()].name
        changeVisibility(View.VISIBLE, next, change, change_label, cost, currency)
        next.text = element.name
        change.text = changeText
        cost.text = money.toString()
        currency.setImageBitmap(context.getCurrencyIcon(money))
        change.setOnClickListener {
            if (player.doingAction)
                return@setOnClickListener

            val enoughFor = player.possessions.enoughFor(possessions)
            if (enoughFor != null) {
                val msg = "Требуется " + when (enoughFor.possession) {
                    PossessionsList.LOCATION -> toast("локация - ${actionsBase.locations[enoughFor.value].name}")
                    PossessionsList.TRANSPORT -> toast("транспорт - ${actionsBase.transports[enoughFor.value].name}")
                    PossessionsList.FRIEND -> toast("кореш - ${actionsBase.friends[enoughFor.value].name}")
                    PossessionsList.HOME -> toast("дом - ${actionsBase.homes[enoughFor.value].name}")
                }

                toast(msg)
                return@setOnClickListener
            }

            if (course != -1 && player.courses[course] < actionsBase.courses[course].length) {
                toast("Требуется пройти курс - ${actionsBase.courses[course].name}")
                return@setOnClickListener
            }
            if (!player.addMoney(money)) {
                toast(getString(R.string.money_needed))
                return@setOnClickListener
            }

            ref.set(ref.get() + 1)
            install(name0, icon0, changeText, elements, ref)
            toast("Выполнено!")
            ActionsListFragment.updateActions()
        }
    }
}
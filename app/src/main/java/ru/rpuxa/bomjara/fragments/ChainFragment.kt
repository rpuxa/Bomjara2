package ru.rpuxa.bomjara.fragments

import android.view.View
import kotlinx.android.synthetic.main.chain.view.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.actions.ChainElement
import ru.rpuxa.bomjara.changeVisibility
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.game.player.Possessions
import ru.rpuxa.bomjara.getCurrencyIcon
import ru.rpuxa.bomjara.toast
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
        val possessions = element.possessions
        val money = -element.moneyRemove
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
            if (Player.current.doingAction)
                return@setOnClickListener

            val enoughFor = Player.current.possessions.enoughFor(possessions)
            run {
                when (enoughFor.first) {
                    Possessions.LOCATION -> toast("Требуется локация - ${Actions.locations[enoughFor.second].name}")
                    Possessions.TRANSPORT -> toast("Требуется транспорт - ${Actions.transports[enoughFor.second].name}")
                    Possessions.FRIEND -> toast("Требуется кореш - ${Actions.friends[enoughFor.second].name}")
                    Possessions.HOUSE -> toast("Требуется дом - ${Actions.homes[enoughFor.second].name}")
                    else -> return@run
                }
                return@setOnClickListener
            }

            if (course != -1 && Player.current.courses[course]!! < Actions.courses[course].length) {
                toast("Требуется пройти курс - ${Actions.courses[course].name}")
                return@setOnClickListener
            }
            if (!Player.current.add(money)) {
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
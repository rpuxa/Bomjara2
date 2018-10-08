package ru.rpuxa.bomjara2.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.chain.view.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.game.Player
import ru.rpuxa.bomjara2.game.player.Money
import ru.rpuxa.bomjara2.game.player.Possessions
import ru.rpuxa.bomjara2.toast
import kotlin.reflect.KMutableProperty0

abstract class ChainFragment : Fragment() {

    abstract val layout: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(layout, container, false)

    protected fun View.install(name0: String, icon0: Int, current0: String, next0: String, changeText: String, possessions: Possessions, money: Money, ref: KMutableProperty0<Int>) {
        name.text = name0
        icon.setImageResource(icon0)
        current.text = current0
        next.text = next0
        change.text = changeText
        change.setOnClickListener {
            if (Player.CURRENT.doingAction)
                return@setOnClickListener

            val enoughFor = Player.CURRENT.possessions.enoughFor(possessions)
            run {
                when (enoughFor.first) {
                    Possessions.LOCATION -> toast("Требуется локация - ${Actions.LOCATIONS[enoughFor.second].name}")
                    Possessions.TRANSPORT -> toast("Требуется транспорт - ${Actions.TRANSPORTS[enoughFor.second].name}")
                    Possessions.FRIEND -> toast("Требуется кореш - ${Actions.FRIENDS[enoughFor.second].name}")
                    Possessions.HOUSE -> toast("Требуется дом - ${Actions.HOUSES[enoughFor.second].name}")
                    else -> return@run
                }
                return@setOnClickListener
            }

            if (!Player.CURRENT.add(-money)) {
                toast(getString(R.string.money_needed))
                return@setOnClickListener
            }

            ref.set(ref.get() + 1)
        }
    }
}
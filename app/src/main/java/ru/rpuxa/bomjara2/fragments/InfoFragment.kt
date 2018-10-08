package ru.rpuxa.bomjara2.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.info.view.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.divider
import ru.rpuxa.bomjara2.game.Player

class InfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val player = Player.CURRENT
        view.save_name.text = "хз"
        view.age.text = player.stringAge

        view.location.text = Actions.LOCATIONS[player.possessions.location].name
        view.friend.text = Actions.FRIENDS[player.possessions.friend].name
        view.home.text = Actions.HOUSES[player.possessions.house].name
        view.transport.text = Actions.TRANSPORTS[player.possessions.transport].name

        view.rubles.text = player.money.rubles.divider()
        view.euros.text = player.money.euros.divider()
        view.bitcoins.text = player.money.bitcoins.divider()
        view.bottles.text = player.money.bottles.divider()
        view.diamonds.text = player.money.diamonds.divider()
    }
}
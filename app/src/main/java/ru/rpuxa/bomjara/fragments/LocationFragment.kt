package ru.rpuxa.bomjara.fragments

import android.view.View
import kotlinx.android.synthetic.main.location.view.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.game.Player

class LocationFragment : ChainFragment() {
    override val layout = R.layout.location

    override fun onChange(view: View) {
        view.location_chain.install(
                "Местоположение",
                R.drawable.colored_location,
                "Перейти",
                Actions.LOCATIONS,
                Player.CURRENT.possessions::location
        )

        view.friend_chain.install(
                "Кореш",
                R.drawable.colored_friend,
                "Подружиться",
                Actions.FRIENDS,
                Player.CURRENT.possessions::friend
        )
    }
}
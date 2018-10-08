package ru.rpuxa.bomjara2.fragments

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.location.view.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.game.Player

class LocationFragment : ChainFragment() {
    override val layout = R.layout.location

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var current = Player.CURRENT.possessions.location + 1
        view.location_chain.install(
                "Местоположение",
                R.drawable.colored_location,
                Actions.LOCATIONS[Player.CURRENT.possessions.location].name,
                if (current < Actions.LOCATIONS.size) Actions.LOCATIONS[current].name else "Недоступно",
                "Перейти",
                Player.CURRENT.possessions,
                Player.CURRENT.money,
                Player.CURRENT.possessions::location
        )

        current = Player.CURRENT.possessions.friend + 1
        view.friend_chain.install(
                "Кореш",
                R.drawable.colored_friend,
                Actions.FRIENDS[Player.CURRENT.possessions.friend].name,
                if (current < Actions.FRIENDS.size) Actions.FRIENDS[current].name else "Недоступно",
                "Подружиться",
                Player.CURRENT.possessions,
                Player.CURRENT.money,
                Player.CURRENT.possessions::friend
        )
    }
}
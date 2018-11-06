package ru.rpuxa.bomjara.impl.fragments

import android.view.View
import kotlinx.android.synthetic.main.location.view.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.impl.Data.actionsBase
import ru.rpuxa.bomjara.impl.Data.player

class LocationFragment : ChainFragment() {
    override val layout = R.layout.location

    override fun onChange(view: View) {
        view.location_chain.install(
                "Местоположение",
                R.drawable.colored_location,
                "Перейти",
                actionsBase.locations,
                player.possessions::location
        )

        view.friend_chain.install(
                "Кореш",
                R.drawable.colored_friend,
                "Подружиться",
                actionsBase.friends,
                player.possessions::friend
        )
    }
}
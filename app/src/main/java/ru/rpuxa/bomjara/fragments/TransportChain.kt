package ru.rpuxa.bomjara.fragments

import android.view.View
import kotlinx.android.synthetic.main.transport.view.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.game.Player

class TransportChain : ChainFragment() {
    override val layout = R.layout.transport

    override fun onChange(view: View) {
        view.transport_chain.install(
                "Транспорт",
                R.drawable.colored_transport,
                "Пересесть",
                Actions.transports,
                Player.current.possessions::transport
        )

        view.home_chain.install(
                "Дом",
                R.drawable.colored_home,
                "Переехать",
                Actions.homes,
                Player.current.possessions::home
        )
    }
}
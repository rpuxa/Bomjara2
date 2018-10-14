package ru.rpuxa.bomjara2.fragments

import android.view.View
import kotlinx.android.synthetic.main.transport.view.*
import ru.rpuxa.bomjara2.R
import ru.rpuxa.bomjara2.actions.Actions
import ru.rpuxa.bomjara2.game.Player

class TransportChain : ChainFragment() {
    override val layout = R.layout.transport

    override fun onChange(view: View) {
        view.transport_chain.install(
                "Транспорт",
                R.drawable.colored_transport,
                "Пересесть",
                Actions.TRANSPORTS,
                Player.CURRENT.possessions::transport
        )

        view.home_chain.install(
                "Дом",
                R.drawable.colored_home,
                "Переехать",
                Actions.HOMES,
                Player.CURRENT.possessions::home
        )
    }
}
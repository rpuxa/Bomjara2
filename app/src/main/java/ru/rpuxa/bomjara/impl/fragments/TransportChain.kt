package ru.rpuxa.bomjara.impl.fragments

import android.view.View
import kotlinx.android.synthetic.main.transport.view.*
import ru.rpuxa.bomjara.CurrentData.actionsBase
import ru.rpuxa.bomjara.CurrentData.player
import ru.rpuxa.bomjara.R

class TransportChain : ChainFragment() {
    override val layout = R.layout.transport

    override fun onChange(view: View) {
        view.transport_chain.install(
                "Транспорт",
                R.drawable.colored_transport,
                "Пересесть",
                actionsBase.transports,
                player.possessions::transport
        )

        view.home_chain.install(
                "Дом",
                R.drawable.colored_home,
                "Переехать",
                actionsBase.homes,
                player.possessions::home
        )
    }
}
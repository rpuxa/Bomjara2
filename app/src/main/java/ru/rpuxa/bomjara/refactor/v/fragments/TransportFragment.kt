package ru.rpuxa.bomjara.refactor.v.fragments

import android.view.View
import kotlinx.android.synthetic.main.transport.view.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel
import ru.rpuxa.bomjara.utils.getViewModel

class TransportFragment : ChainFragment() {
    override val layout = R.layout.transport

    override fun onChange(view: View) {
        val viewModel = getViewModel<PlayerViewModel>()

        view.transport_chain.install(
                "Транспорт",
                R.drawable.colored_transport,
                "Пересесть",
                viewModel.transports,
                viewModel::transport
        )

        view.home_chain.install(
                "Дом",
                R.drawable.colored_home,
                "Переехать",
                viewModel.homes,
                viewModel::home
        )
    }
}
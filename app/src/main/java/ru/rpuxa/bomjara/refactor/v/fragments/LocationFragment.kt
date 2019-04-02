package ru.rpuxa.bomjara.refactor.v.fragments

import android.view.View
import kotlinx.android.synthetic.main.location.*
import kotlinx.android.synthetic.main.location.view.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel
import ru.rpuxa.bomjara.utils.getViewModel

class LocationFragment : ChainFragment() {
    override val layout = R.layout.location

    override fun onChange(view: View) {
        val viewModel = getViewModel<PlayerViewModel>()
        location_chain.install(
                "Местоположение",
                R.drawable.colored_location,
                "Перейти",
                viewModel.locations,
                viewModel::location
        )


        view.friend_chain.install(
                "Кореш",
                R.drawable.colored_friend,
                "Подружиться",
                viewModel.friends,
                viewModel::friend
        )

        TipFragment.bind(this, R.id.tip_location)
    }
}
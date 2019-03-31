package ru.rpuxa.bomjara.refactor.v.fragments

import android.annotation.SuppressLint
import android.view.View
import kotlinx.android.synthetic.main.info.*
import org.jetbrains.anko.support.v4.browse
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.refactor.vm.PlayerViewModel
import ru.rpuxa.bomjara.utils.ageToString
import ru.rpuxa.bomjara.utils.divider
import ru.rpuxa.bomjara.utils.getViewModel
import ru.rpuxa.bomjara.utils.observe

class InfoFragment : CacheFragment() {


    override val layout = R.layout.info

    @SuppressLint("SetTextI18n")
    override fun onChange(view: View) {
        val viewModel = getViewModel<PlayerViewModel>()
       save_name.text = viewModel.name
        viewModel.age.observe(this) { age.text = ageToString(it) }
        viewModel.location.observe(this) { location.text = viewModel.getLocation(it).name }
        viewModel.friend.observe(this) { friend.text = viewModel.getFriend(it).name }
        viewModel.home.observe(this) { home.text = viewModel.getHome(it).name }
        viewModel.transport.observe(this) { transport.text = viewModel.getTransport(it).name }
        viewModel.money.observe(this) { money ->
            rubles.text = money.rubles.divider()
            bottles.text = money.bottles.divider()
            diamonds.text = money.diamonds.divider()
        }
        viewModel.efficiency.observe(this) { efficiency.text = "$it%" }
        viewModel.maxCondition.observe(this) {
            max_energy.text = it.energy.toString()
            max_fullness.text = it.fullness.toString()
            max_health.text = it.health.toString()
        }
        vk.setOnClickListener {
            browse(getString(R.string.vk_link))
        }
    }
}
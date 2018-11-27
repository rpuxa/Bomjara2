package ru.rpuxa.bomjara.impl.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.info.*
import org.jetbrains.anko.browse
import ru.rpuxa.bomjara.CurrentData.actionsBase
import ru.rpuxa.bomjara.CurrentData.player
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.utils.ageToString
import ru.rpuxa.bomjara.utils.divider

class InfoFragment : CacheFragment() {


    override val layout = R.layout.info

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            save_name.text = player.name
            age.text = ageToString(player.age)

            location.text = actionsBase.locations[player.possessions.location].name
            friend.text = actionsBase.friends[player.possessions.friend].name
            home.text = actionsBase.homes[player.possessions.home].name
            transport.text = actionsBase.transports[player.possessions.transport].name

            rubles.text = player.money.rubles.divider()
            euros.text = player.money.euros.divider()
            bitcoins.text = player.money.bitcoins.divider()
            bottles.text = player.money.bottles.divider()
            diamonds.text = player.money.diamonds.divider()

            efficiency.text = "${player.efficiency}%"
            max_energy.text = player.maxCondition.energy.toString()
            max_fullness.text = player.maxCondition.fullness.toString()
            max_health.text = player.maxCondition.health.toString()

            vk.setOnClickListener {
                context.browse(getString(R.string.vk_link))
            }
        }
    }

    override fun onChange(view: View) {
    }
}
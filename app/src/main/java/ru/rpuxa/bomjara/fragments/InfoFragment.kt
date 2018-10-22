package ru.rpuxa.bomjara.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.divider
import ru.rpuxa.bomjara.game.Player

class InfoFragment : CacheFragment() {

    override val layout = R.layout.info

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val player = Player.CURRENT
        with(view) {
            save_name.text = player.name
            age.text = player.stringAge

            location.text = Actions.LOCATIONS[player.possessions.location].name
            friend.text = Actions.FRIENDS[player.possessions.friend].name
            home.text = Actions.HOMES[player.possessions.home].name
            transport.text = Actions.TRANSPORTS[player.possessions.transport].name

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
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.vk_link))))

            }
        }
    }

    override fun onChange(view: View) {
    }
}
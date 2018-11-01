package ru.rpuxa.bomjara.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.info.*
import kotlinx.android.synthetic.main.vk_group.view.*
import ru.rpuxa.bomjara.R
import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.browser
import ru.rpuxa.bomjara.divider
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.settings.settings

class InfoFragment : CacheFragment() {

    override val layout = R.layout.info

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val player = Player.current
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
                context.browser(R.string.vk_link)
            }

            vk_card_view.visibility = if (settings.showVkGroupInvite) View.VISIBLE else View.GONE
            vk_card_view.setOnClickListener {
                context.browser(R.string.vk_group_link)
            }
        }
    }

    override fun onChange(view: View) {
    }
}
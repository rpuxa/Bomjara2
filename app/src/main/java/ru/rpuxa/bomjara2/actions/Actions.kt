package ru.rpuxa.bomjara2.actions

import ru.rpuxa.bomjara2.game.player.Money
import ru.rpuxa.bomjara2.game.player.Possessions

object Actions {
    private val actions = ArrayList<LocatedAction>()

    fun generate() {
        for (location in 0..10) {
            for (menu in ENERGY..HEALTH) {
                val action = Action(
                        "",
                        Money()
                )
                actions.add(
                        LocatedAction(location, menu, )
                )
            }
        }
    }

    class LocatedAction(val location: Int, val menu: Int, val action: Action)

    const val ENERGY = 0
    const val FOOD = 1
    const val HEALTH = 2

    private inline fun location(id: Int, block: Location.() -> Unit) {
        val loc = Location(id)
        loc.block()

    }

    class Location(val id: Int) {

        fun friend(name: String, money: Money, possessions: Possessions) {
            actions.add()

            //todo
        }
    }
}
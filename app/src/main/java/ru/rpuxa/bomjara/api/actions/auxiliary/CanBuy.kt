package ru.rpuxa.bomjara.api.actions.auxiliary

import ru.rpuxa.bomjara.api.player.Money

/**
 * Objects that can be bought in game
 */
interface CanBuy {

    val cost: Money
}
package ru.rpuxa.bomjara.api.actions

import ru.rpuxa.bomjara.api.player.Player

typealias VipId = Int

interface Vip {

    val id: VipId

    val name: String

    val cost: Int

    val onBuy: (Player) -> Unit
}
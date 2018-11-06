package ru.rpuxa.bomjara.api.statistic

import ru.rpuxa.bomjara.api.actions.ActionId
import ru.rpuxa.bomjara.api.actions.VipId
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.impl.cache.Savable

interface Statistic : Savable {

    var versionCode: Int

    fun countAction(id: ActionId)

    fun countVip(id: VipId)

    fun loadPlayer(player: Player)

    fun sendStatistic()

    fun sendReview(rating: Float, review: String)
}
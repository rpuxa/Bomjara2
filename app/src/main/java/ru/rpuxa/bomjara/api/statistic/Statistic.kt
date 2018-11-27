package ru.rpuxa.bomjara.api.statistic

import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.cache.Savable

interface Statistic : Savable {

    fun countAction(id: Int)

    fun countVip(id: Int)

    fun loadPlayer(player: Player)

    fun sendStatistic()

    fun sendReview(rating: Float, review: String)
}
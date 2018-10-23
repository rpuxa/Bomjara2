package ru.rpuxa.bomjara.statistic

import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.cache.SuperSerializable
import ru.rpuxa.bomjara.game.Player

object Statistic {
    fun countAction(id: Int) {
        currentStatistic.actionsUsingCount[id]++
    }

    fun load(player: Player) {
        currentStatistic = statistics.find { it.saveId == player.id } ?: CacheStatistic(player)
    }

    fun sendStatistics() = Thread {
        //TODO сделать отправку на будущий сервер
    }.start()

    fun sendReview(rating: Float, review: String) {
        //TODO сделать отправку на будущий сервер

    }

    fun updatePossessions(player: Player) {
        with(player.possessions) {
            currentStatistic.location = location
            currentStatistic.friend = friend
            currentStatistic.transport = transport
            currentStatistic.home = home
        }
    }
}

private lateinit var currentStatistic: IStatistic

private val statistics = ArrayList<IStatistic>()

class CacheStatistic(player: Player) : IStatistic, SuperSerializable {
    override val saveId = player.id
    override var actionsUsingCount = IntArray(Actions.actionsSize)
    override var boughtVips = IntArray(Actions.VIPS.size)
    override var location = player.possessions.location
    override var friend = player.possessions.friend
    override var transport = player.possessions.transport
    override var home = player.possessions.home
}

interface IStatistic {
    val saveId: Long
    var actionsUsingCount: IntArray
    var boughtVips: IntArray
    var location: Int
    var friend: Int
    var transport: Int
    var home: Int
}
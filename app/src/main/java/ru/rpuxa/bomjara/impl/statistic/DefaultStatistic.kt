package ru.rpuxa.bomjara.impl.statistic

import ru.rpuxa.bomjara.api.actions.ActionId
import ru.rpuxa.bomjara.api.actions.VipId
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.api.server.Server
import ru.rpuxa.bomjara.api.statistic.Statistic
import ru.rpuxa.bomjara.impl.Data
import ru.rpuxa.bomjara.impl.Data.player
import ru.rpuxa.bomjara.impl.cache.SuperDeserializator
import ru.rpuxa.bomjara.impl.cache.ToSerialize
import ru.rpuxa.bomjara.impl.readObject
import ru.rpuxa.bomjara.impl.writeObject
import ru.rpuxa.bomjserver.Review
import java.io.File

class DefaultStatistic : Statistic {
    override var versionCode = -1

    override fun countAction(id: ActionId) {
        currentStatistic.actionsUsingCount[id]++
    }


    override fun countVip(id: VipId) {
        currentStatistic.boughtVips[id]++
    }

    override fun loadPlayer(player: Player) {
        var find = statistics.find { it.saveId == player.id }
        if (find == null) {
            find = CachedStatistic(player = player, version = versionCode)
            statistics.add(find)
        }
        currentStatistic = find
    }

    override fun sendStatistic() {
        player.apply {
            currentStatistic.apply {
                val statistic = HashMap<Int, Any>().apply {
                    put(ID, saveId)
                    put(ACTIONS, actionsUsingCount.toIntArray())
                    put(AGE, age)
                    put(LOCATION, possessions.location)
                    put(FRIEND, possessions.friend)
                    put(TRANSPORT, possessions.transport)
                    put(HOME, possessions.home)
                    put(COURSES, courses)
                    put(VIPS, boughtVips.toIntArray())
                    put(VERSION_CODE, version)
                }

                Data.server.send(Server.STATISTIC, statistic)
            }
        }
    }

    override fun sendReview(rating: Float, review: String) {
        Data.server.send(Server.REVIEW, Review(rating, review))
    }

    override fun saveToFile(filesDir: File) {
        filesDir.writeObject(statistics.map { it.serialize() }, FILE)
    }

    override fun loadFromFile(filesDir: File) {
        val readObject = filesDir.readObject<List<ToSerialize>>(FILE)
        statistics = readObject?.map {
            CachedStatistic(SuperDeserializator.deserialize(it) as? CachedStatistic, version = versionCode)
        } as MutableList<CachedStatistic>? ?: return
    }

    private lateinit var currentStatistic: CachedStatistic

    private var statistics: MutableList<CachedStatistic> = ArrayList()

    companion object {
        private const val FILE = "statistic2.0"

        const val ID = 0
        const val ACTIONS = 1
        const val AGE = 2
        const val LOCATION = 3
        const val FRIEND = 4
        const val TRANSPORT = 5
        const val HOME = 6
        const val COURSES = 7
        const val VIPS = 8
        const val VERSION_CODE = 9
    }

}
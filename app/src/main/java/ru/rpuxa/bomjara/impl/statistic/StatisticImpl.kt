package ru.rpuxa.bomjara.impl.statistic

import android.content.Context
import ru.rpuxa.bomjara.CurrentData
import ru.rpuxa.bomjara.CurrentData.player
import ru.rpuxa.bomjara.api.player.Player
import ru.rpuxa.bomjara.api.server.Server
import ru.rpuxa.bomjara.api.statistic.Statistic
import ru.rpuxa.bomjara.cache.SuperDeserializator
import ru.rpuxa.bomjara.cache.ToSerialize
import ru.rpuxa.bomjara.utils.readObject
import ru.rpuxa.bomjara.utils.writeObject
import ru.rpuxa.bomjserver.Review
import java.io.File

object StatisticImpl : Statistic {

    override fun countAction(id: Int) {
        currentStatistic.actionsUsingCount[id]++
    }


    override fun countVip(id: Int) {
        currentStatistic.boughtVips[id]++
    }

    override fun loadPlayer(player: Player) {
        var find = statistics.find { it.saveId == player.id }
        if (find == null) {
            find = CachedStatistic(player = player)
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

                CurrentData.server.send(Server.STATISTIC, statistic)
            }
        }
    }

    override fun sendReview(rating: Float, review: String) {
        CurrentData.server.send(Server.REVIEW, Review(rating, review))
    }

    override fun saveToFile(filesDir: File, context: Context) {
        filesDir.writeObject(statistics.map { it.serialize() }, FILE)
    }

    override fun loadFromFile(filesDir: File, context: Context) {
        val readObject = filesDir.readObject<List<ToSerialize>>(FILE)
        statistics = readObject?.map {
            CachedStatistic(SuperDeserializator.deserialize(it) as? CachedStatistic)
        } as MutableList<CachedStatistic>? ?: return
    }

    private lateinit var currentStatistic: CachedStatistic

    private var statistics: MutableList<CachedStatistic> = ArrayList()

    private const val FILE = "statistic2.0"

    private const val ID = 0
    private const val ACTIONS = 1
    private const val AGE = 2
    private const val LOCATION = 3
    private const val FRIEND = 4
    private const val TRANSPORT = 5
    private const val HOME = 6
    private const val COURSES = 7
    private const val VIPS = 8
    private const val VERSION_CODE = 9

}
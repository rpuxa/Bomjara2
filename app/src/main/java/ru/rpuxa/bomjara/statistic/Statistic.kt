package ru.rpuxa.bomjara.statistic

import ru.rpuxa.bomjara.actions.Actions
import ru.rpuxa.bomjara.cache.SuperDeserializator
import ru.rpuxa.bomjara.cache.SuperSerializable
import ru.rpuxa.bomjara.cache.ToSerialize
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.readObject
import ru.rpuxa.bomjara.writeObject
import ru.rpuxa.bomjserver.Review
import ru.rpuxa.bomjserver.ServerCommand
import java.io.File
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.net.Socket

object Statistic {

    private const val IP_SERVER = "89.223.31.120"
    private const val FILE = "statistic2.0"

    var versionCode = -1

    fun countAction(id: Int) {
        currentStatistic.actionsUsingCount[id]++
    }

    fun countVip(id: Int) {
        currentStatistic.boughtVips[id]++
    }

    fun loadCurrent(player: Player) {
        var find = statistics.find { it.saveId == player.id }
        if (find == null) {
            find = SaveStatistic(player = player)
            statistics.add(find)
        }
        currentStatistic = find
    }

    fun sendStatistics() = Thread {
        Player.CURRENT.apply {
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

                send(STATISTIC, statistic)
            }
        }
    }.start()

    fun sendReview(rating: Float, review: String) = Thread {
        send(REVIEW, Review(rating, review))
    }.start()

    private fun send(id: Int, data: Any) {
        try {
            val socket = Socket()
            socket.connect(InetSocketAddress(IP_SERVER, 7158), 10_000)
            ObjectOutputStream(socket.getOutputStream()).writeObject(ServerCommand(id, data))
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun save(file: File) {
        file.writeObject(statistics.map { it.serialize() }, FILE)
    }

    fun load(file: File, version: Int) {
        versionCode = version
        val readObject = file.readObject<List<ToSerialize>>(FILE)
        statistics = readObject?.map {
            SaveStatistic(SuperDeserializator.deserialize(it) as SaveStatistic)
        } as MutableList<SaveStatistic>? ?: return
    }
}

private lateinit var currentStatistic: SaveStatistic

private var statistics: MutableList<SaveStatistic> = ArrayList()

class SaveStatistic(save: SaveStatistic? = null, player: Player? = null) : SuperSerializable {

    var saveId = -14882342368765
    val version get() = Statistic.versionCode
    var actionsUsingCount: ArrayList<Int> = zeroList(Actions.actionsSize).apply {
        if (save != null)
            for (i in indices)
                set(i, save.actionsUsingCount[i])
    }
    var boughtVips: ArrayList<Int> = zeroList(Actions.VIPS.size).apply {
        if (save != null)
            for (i in indices)
                set(i, save.boughtVips[i])
    }

    init {
        if (save != null)
            saveId = save.saveId
        else if (player != null)
            saveId = player.id
    }
}

fun zeroList(size: Int) = ArrayList<Int>().apply { repeat(size) { add(0) } }


const val STATISTIC = 0
const val REVIEW = 1

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
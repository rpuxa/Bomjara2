package ru.rpuxa.bomjara.actions

import ru.rpuxa.bomjara.currency
import ru.rpuxa.bomjara.game.Player
import ru.rpuxa.bomjara.game.player.Condition
import ru.rpuxa.bomjara.server.Server
import ru.rpuxa.bomjserver.CachedAction
import ru.rpuxa.bomjserver.CachedChainElement
import ru.rpuxa.bomjserver.CachedCourse
import java.io.*
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.properties.Delegates


object Actions {
    const val ENERGY = 0
    const val FOOD = 1
    const val HEALTH = 2
    const val JOBS = 3

    private const val ACTIONS = 0
    private const val LOCATIONS = 1
    private const val FRIENDS = 2
    private const val TRANSPORTS = 3
    private const val HOMES = 4
    private const val COURSES = 5
    private const val HASH = 6


    lateinit var actions: Array<Action>
    lateinit var locations: Array<ChainElement>
    lateinit var friends: Array<ChainElement>
    lateinit var transports: Array<ChainElement>
    lateinit var homes: Array<ChainElement>
    lateinit var courses: Array<Course>
    private var hash by Delegates.notNull<Int>()

    //НЕ менять количество!!1
    val vips = arrayOf(
            Vip(0, "+10 к макс. запасу сытости", 9) {
                it.maxCondition.fullness += 10
                Player.current.listener?.onMaxConditionChanged()
            },
            Vip(1, "+10 к макс. запасу здоровья", 9) {
                it.maxCondition.health += 10
                Player.current.listener?.onMaxConditionChanged()

            },
            Vip(2, "+10 к макс. запасу бодрости", 9) {
                it.maxCondition.energy += 10
                Player.current.listener?.onMaxConditionChanged()
            },
            Vip(3, "+10% к эффективности работы", 15) {
                it.efficiency += 10
            }
    )

    private val penalties = arrayOf(
            500, 3_000, 6_000, 25_000, 50_000, 150_000, 350_000
    )

    operator fun get(menu: Int): List<Action> {
        val level = if (menu == JOBS) Player.current.possessions.friend else Player.current.possessions.location
        return actions.filter { it.level == level && it.menu == menu }
    }

    val penalty get() = penalties[Player.current.possessions.location]

    fun getMenuName(menu: Int) = when (menu) {
        ENERGY -> "Бодрость"
        FOOD -> "Еда"
        HEALTH -> "Здоровье"
        else -> "Работа"
    }

    private const val SERVER_ACTIONS_FILE = "server_actions.bomj"

    @Suppress("UNCHECKED_CAST")
    fun load(file: File, input: InputStream) {
        fun Array<CachedChainElement>.toElements(): Array<ChainElement> {
            return map {
                ChainElement(
                        it.name, it.transport.toInt(), it.home.toInt(), it.friend.toInt(), it.location.toInt(),
                        it.course.toInt(), (-it.cost).toLong().currency(it.currency.toInt())
                )
            }.toTypedArray()
        }

        fun Array<CachedAction>.toActions(): Array<Action> {
            return map { a ->
                Action(
                        a.id.toInt(), a.level.toInt(), a.menu.toInt(), a.name, (-a.cost).toLong().currency(a.currency.toInt()),
                        Condition(a.energy.toInt(), a.food.toInt(), a.health.toInt()), a.isIllegal
                )
            }.toTypedArray()
        }

        fun Array<CachedCourse>.toCourses(): Array<Course> {
            return map {
                Course(it.id.toInt(), it.name, (-it.cost).toLong().currency(it.currency.toInt()), it.length.toInt())
            }.toTypedArray()
        }

        fun loadFile(inputStream: InputStream): Boolean {
            return try {
                ObjectInputStream(inputStream).use {
                    val actions = it.readObject() as Array<CachedAction>
                    val locations = it.readObject() as Array<CachedChainElement>
                    val friends = it.readObject() as Array<CachedChainElement>
                    val transports = it.readObject() as Array<CachedChainElement>
                    val homes = it.readObject() as Array<CachedChainElement>
                    val courses = it.readObject() as Array<CachedCourse>
                    val hash = it.readObject() as Int

                    Actions.actions = actions.toActions()
                    Actions.locations = locations.toElements()
                    Actions.friends = friends.toElements()
                    Actions.transports = transports.toElements()
                    Actions.homes = homes.toElements()
                    Actions.courses = courses.toCourses()
                    Actions.hash = hash
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun loadFile(file: File): Boolean {
            try {
                FileInputStream(file).use { return loadFile(it) }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }

        val serverFile = File(file, SERVER_ACTIONS_FILE)
        if (!loadFile(serverFile))
            loadFile(input)

        save(serverFile)
    }

    fun save(file: File) {
        try {
            val cachedActions = cachedActions!!
            val cachedLocations = cachedLocations!!
            val cachedFriends = cachedFriends!!
            val cachedTransports = cachedTransports!!
            val cachedHomes = cachedHomes!!
            val cachedCourses = cachedCourses!!
            val cachedHash = cachedHash!!

            ObjectOutputStream(FileOutputStream(File(file, SERVER_ACTIONS_FILE))).use {
                it.writeObject(cachedActions)
                it.writeObject(cachedLocations)
                it.writeObject(cachedFriends)
                it.writeObject(cachedTransports)
                it.writeObject(cachedHomes)
                it.writeObject(cachedCourses)
                it.writeObject(cachedHash)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var cachedActions = null as Array<CachedAction>?
    private var cachedLocations = null as Array<CachedChainElement>?
    private var cachedFriends = null as Array<CachedChainElement>?
    private var cachedTransports = null as Array<CachedChainElement>?
    private var cachedHomes = null as Array<CachedChainElement>?
    private var cachedCourses = null as Array<CachedCourse>?
    private var cachedHash = null as Int?

    @Suppress("UNCHECKED_CAST")
    fun loadFromServer() {
        thread(isDaemon = true) {
            Server.send(Server.GET_CACHED_ACTIONS, HASH) { h ->
                if (h != null && h.data as Int != hash) {
                    cachedHash = h.data as Int
                    Server.send(Server.GET_CACHED_ACTIONS, ACTIONS) { cachedActions = it?.data as? Array<CachedAction>? }
                    Server.send(Server.GET_CACHED_ACTIONS, LOCATIONS) { cachedLocations = it?.data as? Array<CachedChainElement>? }
                    Server.send(Server.GET_CACHED_ACTIONS, FRIENDS) { cachedFriends = it?.data as? Array<CachedChainElement>? }
                    Server.send(Server.GET_CACHED_ACTIONS, TRANSPORTS) { cachedTransports = it?.data as? Array<CachedChainElement>? }
                    Server.send(Server.GET_CACHED_ACTIONS, HOMES) { cachedHomes = it?.data as? Array<CachedChainElement>? }
                    Server.send(Server.GET_CACHED_ACTIONS, COURSES) { cachedCourses = it?.data as? Array<CachedCourse>? }
                }
            }
        }
    }
}